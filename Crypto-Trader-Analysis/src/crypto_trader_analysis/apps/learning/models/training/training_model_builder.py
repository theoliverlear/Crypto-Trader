from typing import TYPE_CHECKING

from attr import attr
from attrs import define

from src.crypto_trader_analysis.apps.learning.models.database.query_type import QueryType
from src.crypto_trader_analysis.apps.learning.models.training.specs.batch_size_evaluations import BatchSizeEvaluations
from src.crypto_trader_analysis.apps.learning.models.training.specs.dataset_size import DatasetSize
from src.crypto_trader_analysis.apps.learning.models.training.specs.epoch_focus import EpochFocus
from src.crypto_trader_analysis.apps.learning.models.training.specs.sequence_length_sentiment import \
    SequenceLengthSentiment
if TYPE_CHECKING:
    from src.crypto_trader_analysis.apps.learning.models.training.training_model import TrainingModel

@define
class TrainingModelBuilder:
    _max_rows: int = attr(default=2000)
    _epochs: int = attr(default=35)
    _batch_size: int = attr(default=32)
    _skip_small_samples: bool = attr(default=True)
    _sequence_length: int = attr(default=10)
    _query_type: QueryType = attr(default=QueryType.HISTORICAL_PRICE)

    def max_rows(self, max_rows: DatasetSize) -> 'TrainingModelBuilder':
        self._max_rows = max_rows.value
        return self

    def epochs(self, epochs: EpochFocus) -> 'TrainingModelBuilder':
        self._epochs = epochs.value
        return self

    def batch_size(self, batch_size: BatchSizeEvaluations) -> 'TrainingModelBuilder':
        self._batch_size = batch_size.value
        return self

    def skip_small_samples(self, skip_small_samples: bool) -> 'TrainingModelBuilder':
        self._skip_small_samples = skip_small_samples
        return self

    def sequence_length(self, sequence_length: SequenceLengthSentiment | int) -> 'TrainingModelBuilder':
        if isinstance(sequence_length, SequenceLengthSentiment):
            sequence_length = sequence_length.value
        else:
            sequence_length = int(sequence_length)
        self._sequence_length = sequence_length
        return self

    def query_type(self, query_type: QueryType) -> 'TrainingModelBuilder':
        self._query_type = query_type
        return self

    def build(self) -> 'TrainingModel':
        from src.crypto_trader_analysis.apps.learning.models.training.training_model import TrainingModel
        return TrainingModel(
            max_rows=self._max_rows,
            epochs=self._epochs,
            batch_size=self._batch_size,
            skip_small_samples=self._skip_small_samples,
            sequence_length=self._sequence_length,
            query_type=self._query_type
        )