from typing import TYPE_CHECKING

from attr import attr
from attrs import define

if TYPE_CHECKING:
    from src.crypto_trader_analysis.apps.learning.models.training.multi_layer_training_model import \
        MultiLayerTrainingModel
from src.crypto_trader_analysis.apps.learning.models.training.training_model_builder import TrainingModelBuilder


@define
class MultiLayerTrainingModelBuilder(TrainingModelBuilder):
    _short_seq_length: int = attr(default=10)
    _medium_seq_length: int = attr(default=50)
    _long_seq_length: int = attr(default=150)
    
    def build(self) -> 'MultiLayerTrainingModel':
        return MultiLayerTrainingModel(
            max_rows=self._max_rows,
            epochs=self._epochs,
            batch_size=self._batch_size,
            skip_small_samples=self._skip_small_samples,
            sequence_length=self._sequence_length,
            query_type=self._query_type,
            short_seq_length=self._short_seq_length,
            medium_seq_length=self._medium_seq_length,
            long_seq_length=self._long_seq_length
        )