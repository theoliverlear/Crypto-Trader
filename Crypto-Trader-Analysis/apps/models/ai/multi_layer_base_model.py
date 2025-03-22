from abc import ABC

from attr import attr
from attrs import define

from apps.models.ai.base_model import BaseModel


@define
class MultiLayerBaseModel(BaseModel, ABC):
    short_seq_len: int = attr(default=10)
    medium_seq_len: int = attr(default=50)
    long_seq_len: int = attr(default=150)