from enum import Enum


class SequenceLengthSentiment(Enum):
    MICRO_TRENDS: int = 10
    MINI_TRENDS: int = 20
    SIGNAL_TRENDS: int = 30
    CYCLIC_EVENTS: int = 50
    LONG_TRENDS: int = 75
    SMALL_SENTIMENT: int = 125
    STANDARD_SENTIMENT: int = 150
    LARGE_SENTIMENT: int = 200
    SEASONALITY: int = 300