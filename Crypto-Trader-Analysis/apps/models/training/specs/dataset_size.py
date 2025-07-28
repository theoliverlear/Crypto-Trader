from enum import Enum


class DatasetSize(Enum):
    MICRO = 15_000
    MINI = 30_000
    SMALL = 75_000
    MEDIUM = 112_500
    STANDARD = 225_000
    LARGE = 375_000
    EXTRA_LARGE = 600_000
    HUGE = 750_000
    MASSIVE = 1_500_000