from os import getenv
from sqlalchemy import create_engine


class Database:
    url: str = getenv("PSQL_DB_URL")
    user: str = getenv("PSQL_USER")
    password: str = getenv("PSQL_PW")
    name: str = "crypto_trader"
    port: str = "5432"
    host: str = "localhost"
    def __init__(self):
        self.engine = self.build_database_engine()

    def create_engine(self):
        self.engine = create_engine(self.get_engine_url())

    @staticmethod
    def get_engine_url():
        return f"postgresql+psycopg2://{Database.user}:{Database.password}@{Database.host}:{Database.port}/{Database.name}"