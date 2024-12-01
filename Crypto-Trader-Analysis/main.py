from src.database.database import Database
from src.database.query import Query

if __name__ == "__main__":
    database = Database()
    query_text = "SELECT currency_code, last_updated, currency_value FROM currency_history WHERE currency_code = 'BTC' ORDER BY last_updated"
    query = Query(query_text)
    result = database.query_database(query)
    print(result)
    database.disconnect()