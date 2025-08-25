from enum import Enum

from src.crypto_trader_analysis.apps.learning.models.ai.lstm.base_model import BaseModel
from src.crypto_trader_analysis.apps.learning.models.ai.lstm.complex_lstm_model import ComplexLstmModel
from src.crypto_trader_analysis.apps.learning.models.ai.lstm.layered.complex_multi_layer_lstm_model import \
    ComplexMultiLayerLstmModel
from src.crypto_trader_analysis.apps.learning.models.ai.lstm.lstm_model import LstmModel
from src.crypto_trader_analysis.apps.learning.models.ai.lstm.layered.multi_layer_lstm_model import MultiLayerLstmModel


class ModelType(Enum):
    LSTM = LstmModel
    COMPLEX_LSTM = ComplexLstmModel
    MULTI_LAYER = MultiLayerLstmModel
    COMPLEX_MULTI_LAYER = ComplexMultiLayerLstmModel

    @staticmethod
    def from_instance(base_model: BaseModel) -> "ModelType":
        if isinstance(base_model, LstmModel):
            return ModelType.LSTM
        elif isinstance(base_model, ComplexLstmModel):
            return ModelType.COMPLEX_LSTM
        elif isinstance(base_model, MultiLayerLstmModel):
            return ModelType.MULTI_LAYER
        elif isinstance(base_model, ComplexMultiLayerLstmModel):
            return ModelType.COMPLEX_MULTI_LAYER
        else:
            raise ValueError(f"Unknown model type: {type(base_model)}")

    def __str__(self):
        if self == ModelType.LSTM:
            return "LSTM Model"
        elif self == ModelType.COMPLEX_LSTM:
            return "Complex LSTM Model"
        elif self == ModelType.MULTI_LAYER:
            return "Multi-Layer LSTM Model"
        elif self == ModelType.COMPLEX_MULTI_LAYER:
            return "Complex Multi-Layer LSTM Model"
        else:
            raise ValueError(f"Unknown model type: {self}")

    def to_entity_str(self) -> str:
        match self:
            case ModelType.LSTM:
                return "lstm"
            case ModelType.COMPLEX_LSTM:
                return "complex_lstm"
            case ModelType.MULTI_LAYER:
                return "multi_layer"
            case ModelType.COMPLEX_MULTI_LAYER:
                return "complex_multi_layer"
            case _:
                raise ValueError(f"Unknown model type: {self}")