# training_type.py
from enum import Enum

from src.crypto_trader_analysis.apps.learning.models.database.query_type import QueryType
from src.crypto_trader_analysis.apps.learning.models.training.training_model import TrainingModel

# TODO: Replace loading programmatically with loading a YAML file.

class TrainingType(Enum):
    # TODO: Add prediction type model with number of rows as field.
    # Detailed Types
    LARGE_DATA_DETAILED_TRAINING = TrainingModel(
        max_rows=30000, epochs=100, batch_size=16, skip_small_samples=True, sequence_length=10, query_type=QueryType.HISTORICAL_PRICE
    )
    ALL_DATA_DETAILED_TRAINING = TrainingModel(
        max_rows=1_800_000, epochs=100, batch_size=32, skip_small_samples=True, sequence_length=10, query_type=QueryType.HISTORICAL_PRICE
    )
    HISTORICAL_LARGE_DATA_DETAILED_TRAINING = TrainingModel(
        max_rows=40000, epochs=100, batch_size=16, skip_small_samples=True, sequence_length=10, query_type=QueryType.HISTORICAL_PRICE_SPACED
    )


    # Keyword 1: Detailed = training for micro-trends, Balanced = training for
    #            short and long term trends, Generalized = training for
    #            long-term trends
    # Keyword 2: Short = time to train and fetch data is short, Medium = time
    #            to train and fetch data is moderately long, Long = time to
    #            train and fetch data is long
    LARGEST_DATA_DETAILED_TRAINING = TrainingModel(
        max_rows=40000, epochs=100, batch_size=32, skip_small_samples=True, sequence_length=10
    )
    SUPER_LARGE_DATA_DETAILED_TRAINING = TrainingModel(
        max_rows=65000, epochs=100, batch_size=32, skip_small_samples=True, sequence_length=10
    )
    MINI_DATA_DETAILED_TRAINING = TrainingModel(
        max_rows=1000, epochs=100, batch_size=32, skip_small_samples=True, sequence_length=10
    )
    SMALL_DATA_DETAILED_TRAINING = TrainingModel(
        max_rows=5000, epochs=100, batch_size=32, skip_small_samples=True, sequence_length=10
    )
    MODERATE_DATA_DETAILED_TRAINING = TrainingModel(
        max_rows=10000, epochs=100, batch_size=16, skip_small_samples=True, sequence_length=10
    )
    MOST_DATA_DETAILED_TRAINING = TrainingModel(
        max_rows=75000, epochs=100, batch_size=32, skip_small_samples=True, sequence_length=10
    )
    ALL_DATA_GENERAL_TRAINING = TrainingModel(
        max_rows=1000000, epochs=100, batch_size=128, skip_small_samples=True, sequence_length=60
    )
    # LARGE_DATA_DETAILED_TRAINING = TrainingModel(
    #     max_rows=30000, epochs=100, batch_size=32, skip_small_samples=True, sequence_length=10
    # )
    MEDIUM_DATA_DETAILED_TRAINING = TrainingModel(
        max_rows=15000, epochs=100, batch_size=16, skip_small_samples=True, sequence_length=10
    )


    # Small amount of training data, short training time
    DETAILED_SHORT_TRAINING = TrainingModel(
        max_rows=1000, epochs=25, batch_size=32, skip_small_samples=False, sequence_length=10
    )
    BALANCED_SHORT_TRAINING = TrainingModel(
        max_rows=1500, epochs=20, batch_size=64, skip_small_samples=True, sequence_length=15
    )
    GENERALIZED_SHORT_TRAINING = TrainingModel(
        max_rows=2000, epochs=15, batch_size=128, skip_small_samples=True, sequence_length=20
    )
    # Medium amount of training data, moderate training time
    DETAILED_MEDIUM_TRAINING = TrainingModel(
        max_rows=5000, epochs=50, batch_size=32, skip_small_samples=False, sequence_length=30
    )
    BALANCED_MEDIUM_TRAINING = TrainingModel(
        max_rows=7500, epochs=40, batch_size=64, skip_small_samples=True, sequence_length=50
    )
    GENERALIZED_MEDIUM_TRAINING = TrainingModel(
        max_rows=10000, epochs=35, batch_size=128, skip_small_samples=True, sequence_length=75
    )
    # Large amount of training data, long training time
    DETAILED_LONG_TRAINING = TrainingModel(
        max_rows=25000, epochs=75, batch_size=32, skip_small_samples=False, sequence_length=100
    )
    BALANCED_LONG_TRAINING = TrainingModel(
        max_rows=50000, epochs=60, batch_size=64, skip_small_samples=True, sequence_length=150
    )
    GENERALIZED_LONG_TRAINING = TrainingModel(
        max_rows=100000, epochs=50, batch_size=128, skip_small_samples=True, sequence_length=200
    )
    # Keyword 1: Recent = past number of rows, Historical = past number of
    #            rows since the beginning of the database
    # Keyword 2: Detailed = training for micro-trends, Balanced = training for
    #            short and long term trends, Generalized = training for
    #            long-term trends
    # Keyword 3: Short = time to train and fetch data is short, Medium = time
    #            to train and fetch data is moderately long, Long = time to
    #            train and fetch data is long

    RECENT_DETAILED_SHORT_TRAINING = TrainingModel(
        max_rows=1000, epochs=25, batch_size=32, skip_small_samples=False,
        sequence_length=10, query_type=QueryType.HISTORICAL_PRICE
    )
    RECENT_BALANCED_SHORT_TRAINING = TrainingModel(
        max_rows=1500, epochs=20, batch_size=64, skip_small_samples=True,
        sequence_length=15, query_type=QueryType.HISTORICAL_PRICE
    )
    RECENT_GENERALIZED_SHORT_TRAINING = TrainingModel(
        max_rows=2000, epochs=15, batch_size=128, skip_small_samples=True,
        sequence_length=20, query_type=QueryType.HISTORICAL_PRICE
    )
    RECENT_DETAILED_MEDIUM_TRAINING = TrainingModel(
        max_rows=5000, epochs=50, batch_size=32, skip_small_samples=False,
        sequence_length=30, query_type=QueryType.HISTORICAL_PRICE
    )
    RECENT_BALANCED_MEDIUM_TRAINING = TrainingModel(
        max_rows=7500, epochs=40, batch_size=64, skip_small_samples=True,
        sequence_length=50, query_type=QueryType.HISTORICAL_PRICE
    )
    RECENT_GENERALIZED_MEDIUM_TRAINING = TrainingModel(
        max_rows=10000, epochs=35, batch_size=128, skip_small_samples=True,
        sequence_length=75, query_type=QueryType.HISTORICAL_PRICE
    )
    RECENT_DETAILED_LONG_TRAINING = TrainingModel(
        max_rows=25000, epochs=75, batch_size=32, skip_small_samples=False,
        sequence_length=100, query_type=QueryType.HISTORICAL_PRICE
    )
    RECENT_BALANCED_LONG_TRAINING = TrainingModel(
        max_rows=50000, epochs=60, batch_size=64, skip_small_samples=True,
        sequence_length=150, query_type=QueryType.HISTORICAL_PRICE
    )
    RECENT_GENERALIZED_LONG_TRAINING = TrainingModel(
        max_rows=100000, epochs=50, batch_size=128, skip_small_samples=True,
        sequence_length=200, query_type=QueryType.HISTORICAL_PRICE
    )

    HISTORICAL_DETAILED_SHORT_TRAINING = TrainingModel(
        max_rows=1000, epochs=25, batch_size=32, skip_small_samples=False,
        sequence_length=10, query_type=QueryType.HISTORICAL_PRICE_SPACED
    )
    HISTORICAL_BALANCED_SHORT_TRAINING = TrainingModel(
        max_rows=1500, epochs=20, batch_size=64, skip_small_samples=True,
        sequence_length=15, query_type=QueryType.HISTORICAL_PRICE_SPACED
    )
    HISTORICAL_GENERALIZED_SHORT_TRAINING = TrainingModel(
        max_rows=2000, epochs=15, batch_size=128, skip_small_samples=True,
        sequence_length=20, query_type=QueryType.HISTORICAL_PRICE_SPACED
    )
    HISTORICAL_DETAILED_MEDIUM_TRAINING = TrainingModel(
        max_rows=5000, epochs=50, batch_size=32, skip_small_samples=False,
        sequence_length=30, query_type=QueryType.HISTORICAL_PRICE_SPACED
    )
    HISTORICAL_BALANCED_MEDIUM_TRAINING = TrainingModel(
        max_rows=7500, epochs=40, batch_size=64, skip_small_samples=True,
        sequence_length=50, query_type=QueryType.HISTORICAL_PRICE_SPACED
    )
    HISTORICAL_GENERALIZED_MEDIUM_TRAINING = TrainingModel(
        max_rows=10000, epochs=35, batch_size=128, skip_small_samples=True,
        sequence_length=75, query_type=QueryType.HISTORICAL_PRICE_SPACED
    )
    HISTORICAL_DETAILED_LONG_TRAINING = TrainingModel(
        max_rows=25000, epochs=75, batch_size=32, skip_small_samples=False,
        sequence_length=100, query_type=QueryType.HISTORICAL_PRICE_SPACED
    )
    HISTORICAL_BALANCED_LONG_TRAINING = TrainingModel(
        max_rows=50000, epochs=60, batch_size=64, skip_small_samples=True,
        sequence_length=150, query_type=QueryType.HISTORICAL_PRICE_SPACED
    )
    HISTORICAL_GENERALIZED_LONG_TRAINING = TrainingModel(
        max_rows=100000, epochs=50, batch_size=128, skip_small_samples=True,
        sequence_length=200, query_type=QueryType.HISTORICAL_PRICE_SPACED
    )

