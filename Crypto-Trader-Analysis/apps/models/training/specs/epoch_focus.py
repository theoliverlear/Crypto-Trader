from enum import Enum


class EpochFocus(Enum):
    INSTANT: int = 1
    SMALL_VALIDATION: int = 5
    SMALL_ANALYSIS: int = 10
    STANDARD_VALIDATION: int = 20
    REFINED_VALIDATION: int = 30
    STANDARD_ANALYSIS: int = 50
    REFINED_ANALYSIS: int = 75
    DEEP_ANALYSIS: int = 100