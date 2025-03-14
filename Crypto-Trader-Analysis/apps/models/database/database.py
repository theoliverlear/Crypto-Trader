# database.py
import logging
from os import getenv

import pandas as pd
from attr import attr
from attrs import define
from sqlalchemy import create_engine, QueuePool


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
        self.engine = create_engine(
            f"postgresql+psycopg2://{self.user}:{self.password}@{self.host}:{self.port}/{self.name}",
            poolclass=QueuePool,
            pool_size=10,
            max_overflow=20,
            echo=False
        )

    def _get_default_query(self) -> str:
        return """WITH btc_data AS (
    SELECT last_updated, currency_value AS target_price
    FROM currency_history
    WHERE currency_code = %(target_currency)s
    ORDER BY last_updated DESC LIMIT %(limit)s
)"""
    def _get_default_select_query(self):
        return """ SELECT 
    btc.last_updated,
    btc.target_price,\n"""
    def _get_selection_query(self, currency_code: str):
        currency_code: str = currency_code.lower()
        currency_price: str = currency_code.lower() + '_price'
        return f"COALESCE({currency_code}.currency_value, NULL) AS {currency_price},\n"

    def _get_left_join_query(self, currency_code: str):
        upper_currency_code: str = currency_code.upper()
        lower_currency_code: str = currency_code.lower()
        return f"""LEFT JOIN LATERAL (
    SELECT currency_value FROM currency_history 
    WHERE currency_code = '{upper_currency_code}' AND last_updated BETWEEN btc.last_updated - INTERVAL '2 seconds' AND btc.last_updated + INTERVAL '2 seconds'
    ORDER BY last_updated DESC LIMIT %(limit)s
) {lower_currency_code} ON true\n"""

    def get_full_query(self) -> str:
        currencies: list[str] = ['ETH', 'LTC', 'DOGE', 'ADA', 'XRP', 'DOT',
                                 'BCH', 'LINK', 'UNI', 'BNB', 'USDT', 'EOS',
                                 'TRX', 'XTZ', 'XLM', 'SHIB']
        query: str = self._get_default_query() + self._get_default_select_query()
        for index, currency in enumerate(currencies):
            if index < len(currencies) - 1:
                query += self._get_selection_query(currency)
            else:
                partial_query = self._get_selection_query(currency)
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

    def fetch_data(self, target_currency: str = 'BTC', limit=20000):
        query = self.get_full_query()
        params = {"target_currency": target_currency, "limit": limit}
        try:
            df = pd.read_sql_query(query, self.engine, params=params)
            logging.debug(f"✅ Successfully retrieved {len(df)} rows for {target_currency}")
            return df
        except Exception as exception:
            return pd.DataFrame()

    def close(self):
        self.connection.close()