from enum import Enum


class BatchSizeEvaluations(Enum):
    GENERALIZED: int = 128
    BALANCED: int = 64
    DETAILED: int = 32
    DEEP_ANALYSIS: int = 16