# database.py
import logging
from os import getenv

import pandas as pd
from attr import attr
from attrs import define
from sqlalchemy import create_engine, QueuePool, text

from apps.models.database.query_type import QueryType
from currency_json_generator import get_all_currency_codes


@define
class Database:
    user: str = attr(default=getenv("PSQL_USER"))
    password: str = attr(default=getenv("PSQL_PW"))
    name: str = attr(default="crypto_trader")
    port: str = attr(default="5432")
    host: str = attr(default="localhost")
    connection = attr(default=None)
    engine = attr(default=None)

    def __attrs_post_init__(self):
        connection_url: str = f"postgresql+psycopg2://{self.user}:{self.password}@{self.host}:{self.port}/{self.name}"
        self.engine = create_engine(
            connection_url,
            poolclass=QueuePool,
            pool_size=20,
            max_overflow=40,
            echo=False
        )

    def get_inaccurate_models(self, error_threshold: float,
                              only_recent_predictions: bool = False) -> list[str]:
        # TODO: Refactor to allow for num rows for recent results.
        recent_query = ''
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

    def _get_default_history_query(self) -> str:
        return """WITH btc_data AS (
            SELECT last_updated, currency_value AS target_price
            FROM currency_history
            WHERE currency_code = %(target_currency)s
            ORDER BY last_updated DESC LIMIT %(limit)s
                      )"""
    def _get_default_select_history_query(self):
        return """ SELECT 
    btc.last_updated,
    btc.target_price,\n"""

    def _get_selection_history_query(self, currency_code: str):
        currency_code: str = currency_code.lower()
        currency_price: str = currency_code.lower() + '_price'
        return f"COALESCE({currency_code}.currency_value, NULL) AS {currency_price},\n"

    def _get_left_join_query(self, currency_code: str):
        upper_currency_code: str = currency_code.upper()
        lower_currency_code: str = currency_code.lower()
        return f"""LEFT JOIN LATERAL (
    SELECT currency_value FROM currency_history 
    WHERE currency_code = '{upper_currency_code}' AND last_updated BETWEEN btc.last_updated - INTERVAL '2.5 seconds' AND btc.last_updated + INTERVAL '2.5 seconds'
    ORDER BY last_updated DESC LIMIT 1
) {lower_currency_code} ON true\n"""

    def get_full_history_query(self) -> str:
        currencies: list[str] = list(filter(lambda currency_code: currency_code != "BTC", get_all_currency_codes(True)))
        #     currencies: list[str] = get_all_currency_codes()
        print(currencies)
        query: str = self._get_default_history_query() + self._get_default_select_history_query()
        for index, currency in enumerate(currencies):
            if index < len(currencies) - 1:
                query += self._get_selection_history_query(currency)
            else:
                partial_query = self._get_selection_history_query(currency)
                partial_query = partial_query.replace('price,\n', 'price\n')
                query += partial_query
        query += "FROM btc_data btc\n"
        for index, currency in enumerate(currencies):
            if index < len(currencies) - 1:
                query += self._get_left_join_query(currency)
            else:
                query += self._get_left_join_query(currency) + '\n'
        query += "ORDER BY btc.last_updated DESC LIMIT %(limit)s;"
        return query

    def get_spaced_history_query(self, total_rows: int = 10000) -> str:
        currencies = list(filter(lambda currency_code: currency_code != "BTC",
                                 get_all_currency_codes(True)))

        query = f"""
        WITH ordered_btc_data AS (
            SELECT
                last_updated,
                currency_value AS target_price,
                NTILE({total_rows}) OVER (ORDER BY last_updated) AS bucket
            FROM currency_history
            WHERE currency_code = %(target_currency)s
        ),
        btc_data AS (
            SELECT DISTINCT ON (bucket) last_updated, target_price
            FROM ordered_btc_data
            ORDER BY bucket, last_updated DESC
        )
        SELECT 
            btc.last_updated,
            btc.target_price,\n"""

        for i, currency in enumerate(currencies):
            line = f"COALESCE({currency.lower()}.currency_value, NULL) AS {currency.lower()}_price"
            query += line + (",\n" if i < len(currencies) - 1 else "\n")

        query += "FROM btc_data btc\n"
        for currency in currencies:
            lower = currency.lower()
            upper = currency.upper()
            query += f"""LEFT JOIN LATERAL (
        SELECT currency_value FROM currency_history 
        WHERE currency_code = '{upper}' 
          AND last_updated BETWEEN btc.last_updated - INTERVAL '2.5 seconds' AND btc.last_updated + INTERVAL '2.5 seconds'
        ORDER BY last_updated DESC LIMIT 1
    ) {lower} ON true\n"""

        query += "ORDER BY btc.last_updated DESC;"
        return query

    def fetch_data_in_batches(self,
                              batch_size: int = 1000,
                              target_currency: str = 'BTC',
                              limit: int = 20000,
                              query_type: QueryType = QueryType.HISTORICAL_PRICE) -> list[pd.DataFrame]:
        dataframe: pd.DataFrame = self.fetch_data(target_currency, limit, query_type)
        sliced_dataframes: list[pd.DataFrame] = []
        if len(dataframe) == 0:
            return [pd.DataFrame()]
        for i in range(0, len(dataframe), batch_size):
            batch_df = dataframe.iloc[i:i + batch_size]
            sliced_dataframes.append(batch_df)
        logging.info(f"Fetched {len(dataframe)} rows in {len(sliced_dataframes)} batches.")
        return sliced_dataframes

    def fetch_data(self,
                   target_currency: str = 'BTC',
                   limit=20000,
                   query_type: QueryType = QueryType.HISTORICAL_PRICE) -> pd.DataFrame:
        logging.info("Fetching data from database...")
        query: str = ""
        params: dict = {"target_currency": target_currency}
        if query_type == QueryType.CURRENT_PRICE:
            logging.info("Fetching current price...")
            query = f"SELECT * FROM {query_type.value} WHERE currency_code = %(target_currency)s;"
        elif query_type == QueryType.HISTORICAL_PRICE:
            logging.info("Fetching historical price...")
            query = self.get_full_history_query()
            logging.info("Fetched history query.")
            params = {"target_currency": target_currency, "limit": limit}
        elif query_type == QueryType.HISTORICAL_PRICE_SPACED:
            logging.info("Fetching spaced historical price...")
            query = self.get_spaced_history_query(limit)
            params = {"target_currency": target_currency}
        try:
            logging.debug(f"Executing query...")
            df = pd.read_sql_query(query, self.engine, params=params)
            logging.debug(f"Query executed successfully.")
            logging.debug(f"Successfully retrieved {len(df)} rows for {target_currency}")
            return df
        except Exception as exception:
            return pd.DataFrame()

    def close(self):
        self.connection.close()