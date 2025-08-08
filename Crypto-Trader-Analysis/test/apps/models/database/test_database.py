import time
import unittest
from datetime import datetime

from oauthlib.uri_validate import query
from pandas import DataFrame

from apps.models.database.database import Database
from apps.models.database.query_type import QueryType


def setup_database():
    return Database()

class TestDatabase(unittest.TestCase):
    # def test_get_full_history_query(self):
    #     db = setup_database()
    #     query = db.get_full_history_query()
    #     print(query)
    #     self.assertTrue(True)
    #
    # def test_get_spaced_history_query(self):
    #     db = setup_database()
    #     query = db.get_spaced_history_query()
    #     print(query)
    #     self.assertEqual(True, True)


    def test_fetch_data(self):
        query_start_time = datetime.now()
        db = setup_database()
        df= db.fetch_data("BTC", 500, QueryType.HISTORICAL_PRICE)
        df.to_csv('my_dataframe.csv')
        print(df.head(5))
        print(f"Query took {datetime.now() - query_start_time}")
        self.assertFalse(df.empty)

    def test_fetch_data_spaced(self):
        db = setup_database()
        df = db.fetch_data("BTC", 5000, QueryType.HISTORICAL_PRICE_SPACED)
        df.to_csv('my_dataframe_5_spaced.csv')
        print(df.head(5))
        self.assertFalse(df.empty)

    def test_fetch_data_in_batches(self):
        db = setup_database()
        df = db.fetch_data_in_batches(10, "BTC", 50, QueryType.HISTORICAL_PRICE)
        self.assertEqual(len(df), 5)

    def test_get_inaccurate_models(self):
        db = setup_database()
        currencies = db.get_inaccurate_models(5)
        print(currencies)
        self.assertFalse(len(currencies) == 0)

        currencies = db.get_inaccurate_models(5, True)
        print(currencies)
        self.assertFalse(len(currencies) == 0)

if __name__ == '__main__':
    unittest.main()
