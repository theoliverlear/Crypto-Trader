from enum import Enum


class DatasetSize(Enum):
    MICRO: int = 1000
    MINI: int = 2000
    SMALL: int = 5000
    MEDIUM: int = 7500
    STANDARD: int = 15000
    LARGE: int = 25000
    EXTRA_LARGE: int = 40000
    HUGE: int = 50000
    MASSIVE: int = 100000