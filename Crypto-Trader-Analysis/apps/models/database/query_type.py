from enum import Enum


class QueryType(Enum):
    CURRENT_PRICE = 'current_price'
    HISTORICAL_PRICE = 'historical_price'
    HISTORICAL_PRICE_SPACED = 'historical_price_spaced'