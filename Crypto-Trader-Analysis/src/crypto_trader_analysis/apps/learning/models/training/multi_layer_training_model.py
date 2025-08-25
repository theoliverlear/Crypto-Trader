from typing import TYPE_CHECKING

from attr import attr
from attrs import define

if TYPE_CHECKING:
    from src.crypto_trader_analysis.apps.learning.models.training.multi_layer_training_model_builder import \
        MultiLayerTrainingModelBuilder
from src.crypto_trader_analysis.apps.learning.models.training.training_model import TrainingModel


@define
class MultiLayerTrainingModel(TrainingModel):
    short_seq_length: int = attr(default=10)
    medium_seq_length: int = attr(default=50)
    long_seq_length: int = attr(default=150)

    
    @staticmethod
    def builder() -> 'MultiLayerTrainingModelBuilder':
        from src.crypto_trader_analysis.apps.learning.models.training.multi_layer_training_model_builder import MultiLayerTrainingModelBuilder
        return MultiLayerTrainingModelBuilder()
    
