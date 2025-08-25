from enum import Enum


class BatchSizeEvaluations(Enum):
    GENERALIZED = 128
    BALANCED = 64
    DETAILED = 32
    DEEP_ANALYSIS = 16