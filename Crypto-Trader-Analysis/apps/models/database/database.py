# database.py
import logging
import time
import traceback
from os import getenv

import pandas as pd
from attr import attr
from attrs import define
from sqlalchemy import create_engine, QueuePool, text, Engine

from apps.models.database.query_type import QueryType
from currency_json_generator import get_all_currency_codes
import psycopg2.extras as _pg_extras


@define
class Database:
    #=============================-Variables-=================================
    user: str = attr(default=getenv("PSQL_USER"))
    password: str = attr(default=getenv("PSQL_PW"))
    name: str = attr(default="crypto_trader")
    port: str = attr(default="5432")
    host: str = attr(default="localhost")
    connection = attr(default=None)
    engine: Engine = attr(default=None)

    #---------------------------Attrs-Post-Init-------------------------------
    def __attrs_post_init__(self):
        connection_url: str = self.get_connection_url()
        self.engine: Engine = (
            create_engine(
                connection_url,
                poolclass=QueuePool,
                pool_size=20,
                max_overflow=40,
                echo=True,
                connect_args={
                    "cursor_factory": _pg_extras.NamedTupleCursor,
                },
            )
            .execution_options(stream_results=True)
        )

    def get_connection_url(self) -> str:
        return f"postgresql+psycopg2://{self.user}:{self.password}@{self.host}:{self.port}/{self.name}"

    #------------------------Get-Inaccurate-Models----------------------------
    def get_inaccurate_models(self, error_threshold: float,
                              only_recent_predictions: bool = False) -> list[str]:
        # TODO: Refactor to allow for num rows for recent results.
        recent_query: str = ''
        if only_recent_predictions:
            recent_query = """
                AND last_updated > (NOW() - INTERVAL '1 hour')
            """
        query = f"""
            SELECT
                currency_code,
                AVG(percent_difference) AS avg_difference
            FROM
                predictions
            WHERE
                percent_difference > %(error_threshold)s
            {recent_query}
            GROUP BY
                currency_code
            ORDER BY
                avg_difference DESC;
        """
        params = {"error_threshold": error_threshold}
        try:
            logging.debug(f"Executing query...")
            df = pd.read_sql_query(query, self.engine, params=params)
            logging.debug(f"Query executed successfully.")
        except Exception as exception:
            df = pd.DataFrame()
        return [currency_code for currency_code in df['currency_code'].tolist()]


    #-------------------------Get-Currencies-List-----------------------------
    def get_currencies_list(self) -> list[str]:
        # currencies: list[str] = list(filter(lambda currency_code: currency_code != "BTC",
        #                          get_all_currency_codes(True)))
        # return currencies
        return get_all_currency_codes(True)

    #------------------------Fetch-Data-In-Batches----------------------------
    def fetch_data_in_batches(self,
                              batch_size: int = 10000,
                              target_currency: str = 'BTC',
                              limit: int = 20000,
                              query_type: QueryType = QueryType.HISTORICAL_PRICE) -> list[pd.DataFrame]:
        batch_size = max(batch_size, 10_000)
        dataframe: pd.DataFrame = self.fetch_data(target_currency, limit, query_type)
        sliced_dataframes: list[pd.DataFrame] = []
        if len(dataframe) == 0:
            return [pd.DataFrame()]
        for i in range(0, len(dataframe), batch_size):
            batch_df = dataframe.iloc[i:i + batch_size]
            sliced_dataframes.append(batch_df)
        logging.info(f"Fetched {len(dataframe)} rows in {len(sliced_dataframes)} batches.")
        return sliced_dataframes

    #-----------------------------Fetch-Data----------------------------------
    def fetch_data(self,
                   target_currency: str = 'BTC',
                   limit=20000,
                   query_type: QueryType = QueryType.HISTORICAL_PRICE,
                   rows_per_log: int = 10_000) -> pd.DataFrame:
        logging.info("Fetching data from database...")
        params, query = self.build_query(limit, query_type, target_currency)
        next_mark: int = 0
        start_time: float = time.time()
        try:
            logging.debug(f"Executing query...")
            return self.execute_query(next_mark, params, query,
                                      rows_per_log, start_time, limit)
        except Exception as exception:
            traceback.print_exc()
            return pd.DataFrame()

    #--------------------------Los-Row-Progress-------------------------------
    def _log_row_progress(self,
                          rows_seen: int,
                          start_time: float,
                          next_mark: int,
                          rows_per_log: int,
                          total_rows: int) -> int:
        while rows_seen >= next_mark:
            elapsed: float = time.time() - start_time or 1e-9
            rows_per_second: float = rows_seen / elapsed
            estimated_total_seconds: float = (total_rows - rows_seen) / rows_per_second
            estimated_formated_str: str = time.strftime("%H:%M:%S", time.gmtime(estimated_total_seconds))
            logging.info("Retrieved %d rows (%.1f rows/sec) | %s remaining...",
                         next_mark,
                         rows_per_second,
                         estimated_formated_str)
            next_mark += rows_per_log
        return next_mark

    #----------------------------Execute-Query--------------------------------
    def execute_query(self,
                      next_mark: int,
                      params, query: str,
                      rows_per_log: int,
                      start_time: float,
                      limit: int = 10000,
                      batch_size = 1000) -> pd.DataFrame:
        all_frames: list[pd.DataFrame] = []
        streamed_rows: int = 0
        with self.engine.connect().execution_options(
                stream_results=True) as connection:
            for chunk in pd.read_sql_query(
                    text(query),
                    connection,
                    params=params,
                    chunksize=batch_size
            ):
                all_frames.append(chunk)
                streamed_rows += len(chunk)
                next_mark = self._log_row_progress(
                    streamed_rows,
                    start_time,
                    next_mark,
                    rows_per_log,
                    limit
                )
                if streamed_rows >= next_mark:
                    logging.info("Retrieved %d rows...", streamed_rows)
                    next_mark += rows_per_log
        return pd.concat(all_frames, ignore_index=True)


    def _price_column_names(self) -> list[str]:
        return [f"{code.lower()}_price" for code in self.get_currencies_list()]


    def _get_select_columns(self):
        price_cols = self._price_column_names()
        select_cols = ["last_updated", *price_cols]
        return ", ".join(select_cols)

    def _get_market_snapshot_query(self) -> str:
        select_list = self._get_select_columns()
        return f"""
            SELECT {select_list}
            FROM   market_snapshots
            ORDER  BY last_updated DESC
            LIMIT  :limit
        """

    def _get_market_snapshot_spaced_query(self, limit: int) -> str:
        select_list = self._get_select_columns()

        return f"""
            WITH bounds AS (
                SELECT MIN(id) AS min_id,
                       MAX(id) AS max_id
                FROM   market_snapshots
            ),
            params AS (
                SELECT
                    GREATEST(
                        ((max_id - min_id + 1 + :limit - 1) / :limit), 1
                    )::bigint                     AS step,
                    min_id,
                    max_id
                FROM bounds
            ),
            ids AS (
                SELECT
                    generate_series(
                        max_id,
                        min_id,
                        -step  
                    ) AS id
                FROM params
                LIMIT :limit   
            )
            SELECT {select_list}
            FROM   market_snapshots  ms
            JOIN   ids USING (id)
            ORDER  BY ms.last_updated DESC;
        """

    #-----------------------------Build-Query---------------------------------
    def build_query(self, limit: int, query_type: QueryType, target_currency: str) -> tuple[dict, str]:
        query: str = ""
        params: dict = {"target_currency": target_currency}
        if query_type == QueryType.CURRENT_PRICE:
            logging.info("Fetching current price...")
            query = f"SELECT * FROM {query_type.value} WHERE currency_code = :target_currency;"
        elif query_type == QueryType.HISTORICAL_PRICE:
            logging.info("Fetching historical price...")
            query = self._get_market_snapshot_query()
            logging.info("Fetched history query.")
            params = {"target_currency": target_currency, "limit": limit}
        elif query_type == QueryType.HISTORICAL_PRICE_SPACED:
            logging.info("Fetching spaced historical price...")
            query = self._get_market_snapshot_spaced_query(limit)
            params = {"target_currency": target_currency, "limit": limit}
        return params, query

    #--------------------------------Close------------------------------------
    def close(self) -> None:
        self.connection.close()