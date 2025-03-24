from enum import Enum

from apps.models.ai.base_model import BaseModel
from apps.models.ai.complex_lstm_model import ComplexLstmModel
from apps.models.ai.complex_multi_layer_lstm_model import \
    ComplexMultiLayerLstmModel
from apps.models.ai.lstm_model import LstmModel
from apps.models.ai.multi_layer_lstm_model import MultiLayerLstmModel


class ModelType(Enum):
    LSTM: BaseModel = LstmModel
    COMPLEX_LSTM: BaseModel = ComplexLstmModel
    MULTI_LAYER: BaseModel = MultiLayerLstmModel
    COMPLEX_MULTI_LAYER: BaseModel = ComplexMultiLayerLstmModel