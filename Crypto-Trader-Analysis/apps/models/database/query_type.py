from enum import Enum


class QueryType(Enum):
    CURRENT_PRICE: str = 'currencies'
    HISTORICAL_PRICE: str = 'currency_history'