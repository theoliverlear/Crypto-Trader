from abc import ABC, abstractmethod

from attr import attr
from attrs import define

from src.crypto_trader_analysis.apps.learning.models.ai.lstm.base_model import BaseModel


@define
class MultiLayerBaseModel(BaseModel, ABC):
    short_seq_len: int = attr(default=10)
    medium_seq_len: int = attr(default=50)
    long_seq_len: int = attr(default=150)

    @staticmethod
    @abstractmethod
    def combine_model(model_input):
        pass