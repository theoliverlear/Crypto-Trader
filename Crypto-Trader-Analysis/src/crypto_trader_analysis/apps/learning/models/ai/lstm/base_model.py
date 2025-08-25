import logging
from abc import ABC, abstractmethod

from attr import attr
from attrs import define
from keras import Sequential, Model
from sklearn.preprocessing import MinMaxScaler
import tensorflow as tf


@define
class BaseModel(ABC):
    dimension = attr(default=None)
    model: Model = attr(factory=Sequential)
    sequence_length: int = attr(default=10)
    target_currency: str = attr(default='BTC')

    @abstractmethod
    def train(self,
              dataset: tf.data.Dataset,
              epochs: int = 20,
              batch_size: int = 32):
        pass

    @abstractmethod
    def predict(self,
                training_data,
                scaler: MinMaxScaler):
        pass

    @abstractmethod
    def save_model(self, path: str):
        pass

    @abstractmethod
    def load_model(self, path: str):
        pass

    @staticmethod
    @abstractmethod
    def get_model_path(target_currency: str) -> str:
        pass

    @staticmethod
    def get_tensorboard_callback(self):
        pass

    def log_model_summary(self):
        logging.info("\n" + "=" * 40 + "\nModel Summary:\n" + "=" * 40)
        self.model.summary(print_fn=lambda print_func: logging.info(print_func))