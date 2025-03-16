# training_model.py
from attr import attr
from attrs import define


@define
class TrainingModel:
    max_rows: int = attr(default=2000)
    epochs: int = attr(default=35)
    batch_size: int = attr(default=32)
    skip_small_samples: bool = attr(default=True)
    sequence_length: int = attr(default=10)
