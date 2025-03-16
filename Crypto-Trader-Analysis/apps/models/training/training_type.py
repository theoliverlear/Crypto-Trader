# training_type.py
from enum import Enum

from apps.models.training.training_model import TrainingModel


class TrainingType(Enum):
    # Detailed Types
    LARGE_DATA_DETAILED_TRAINING: TrainingModel = TrainingModel(
        max_rows=40000, epochs=100, batch_size=16, skip_small_samples=True, sequence_length=10
    )

    # Keyword 1: Detailed = training for micro-trends, Balanced = training for
    #            short and long term trends, Generalized = training for
    #            long-term trends
    # Keyword 2: Short = time to train and fetch data is short, Medium = time
    #            to train and fetch data is moderately long, Long = time to
    #            train and fetch data is long

    DETAILED_SHORT_TRAINING: TrainingModel = TrainingModel(
        max_rows=1000, epochs=25, batch_size=64, skip_small_samples=False, sequence_length=10
    )
    BALANCED_SHORT_TRAINING: TrainingModel = TrainingModel(
        max_rows=1500, epochs=20, batch_size=64, skip_small_samples=True, sequence_length=15
    )
    GENERALIZED_SHORT_TRAINING: TrainingModel = TrainingModel(
        max_rows=2000, epochs=15, batch_size=128, skip_small_samples=True, sequence_length=20
    )

    # Medium Scope: Balances short and long-term trends, moderate dataset size
    DETAILED_MEDIUM_TRAINING: TrainingModel = TrainingModel(
        max_rows=5000, epochs=50, batch_size=32, skip_small_samples=False, sequence_length=30
    )
    BALANCED_MEDIUM_TRAINING: TrainingModel = TrainingModel(
        max_rows=7500, epochs=40, batch_size=64, skip_small_samples=True, sequence_length=50
    )
    GENERALIZED_MEDIUM_TRAINING: TrainingModel = TrainingModel(
        max_rows=10000, epochs=35, batch_size=128, skip_small_samples=True, sequence_length=75
    )

    # Large Scope: Generalized for long-term trends, large dataset size
    DETAILED_LONG_TRAINING: TrainingModel = TrainingModel(
        max_rows=25000, epochs=75, batch_size=32, skip_small_samples=False, sequence_length=100
    )
    BALANCED_LONG_TRAINING: TrainingModel = TrainingModel(
        max_rows=50000, epochs=60, batch_size=64, skip_small_samples=True, sequence_length=150
    )
    GENERALIZED_LONG_TRAINING: TrainingModel = TrainingModel(
        max_rows=100000, epochs=50, batch_size=128, skip_small_samples=True, sequence_length=200
    )