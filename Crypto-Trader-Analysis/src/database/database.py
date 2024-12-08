from os import getenv
import pandas as pd
from sqlalchemy import create_engine

from src.database.query import Query


class Database:
    url: str = getenv("PSQL_DB_URL")
    user: str = getenv("PSQL_USER")
    password: str = getenv("PSQL_PW")
    name: str = "crypto_trader"
    port: str = "5432"
    host: str = "localhost"
    def __init__(self):
        self.engine = self.create_engine()

    def create_engine(self):
        return create_engine(self.get_engine_url())

    def query_database(self, query: Query):
        try:
            df = pd.read_sql(query.query_text, self.engine)
            return self.sanitize_data_frame(df)
        except Exception as e:
            print(f"Error querying database: {e}")
            return None

    def sanitize_data_frame(self, df):
        if not isinstance(df, pd.DataFrame):
            df = pd.DataFrame(df, columns=["currency_code", "last_updated", "currency_value"])
        df["last_updated"] = pd.to_datetime(df["last_updated"])
        return df

    def disconnect(self):
        self.engine.dispose()

    @staticmethod
    def get_engine_url():
        return f"postgresql+psycopg2://{Database.user}:{Database.password}@{Database.host}:{Database.port}/{Database.name}"