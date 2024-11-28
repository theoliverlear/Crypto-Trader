from os import getenv

import pandas as pd
import psycopg2
import matplotlib.pyplot as plt
import plotly.express as plot
from sqlalchemy import create_engine



class DatabaseConnection:
    database_url: str = getenv("PSQL_DB_URL")
    database_user: str = getenv("PSQL_USER")
    database_password: str = getenv("PSQL_PW")
    database_name: str = "CRYPTO_TRADER"
    database_port: str = "5432"
    database_host: str = "localhost"
    def __init__(self):
        self.engine = None

    def connect_to_database(self):
        self.engine = create_engine(f"postgresql+psycopg2://{DatabaseConnection.database_user}:{DatabaseConnection.database_password}@{DatabaseConnection.database_host}:{DatabaseConnection.database_port}/{DatabaseConnection.database_name}")

    def query_database(self, query: str):
        try:
            df = pd.read_sql(query, self.engine)
            if not isinstance(df, pd.DataFrame):
                df = pd.DataFrame(df, columns=['currency_code', 'last_updated', 'currency_value'])
            df['last_updated'] = pd.to_datetime(df['last_updated'])
            return df
        except Exception as e:
            print(f"Error querying database: {e}")
            return None
    def close_database_connection(self):
        if self.engine:
            self.engine.dispose()

    @staticmethod
    def process_data(data):
        timestamps = [row[1] for row in data]
        values = [float(row[2]) for row in data]
        return timestamps, values
    @staticmethod
    def plot_data(df):
        # Plot the data
        plt.figure(figsize=(12, 6))
        plt.plot(df["last_updated"], df["currency_value"], marker="o", linestyle="-", label="BTC Value")
        plt.title("BTC Value Over Time")
        plt.xlabel("Timestamp")
        plt.ylabel("Value (USD)")
        plt.xticks(rotation=45)  # Rotate x-axis labels
        plt.grid(True)
        plt.legend()
        plt.tight_layout()
        plt.show()

if __name__ == "__main__":
    db_connection = DatabaseConnection()
    db_connection.connect_to_database()
    query = "SELECT currency_code, last_updated, currency_value FROM currency_history WHERE currency_code = 'BTC' ORDER BY last_updated"
    result = db_connection.query_database(query)
    db_connection.close_database_connection()
    if result is not None and not result.empty:
        db_connection.plot_data(result)
        print(result)
