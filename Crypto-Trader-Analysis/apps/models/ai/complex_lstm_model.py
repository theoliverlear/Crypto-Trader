# lstm_model.py
import logging
from abc import ABC
from typing_extensions import override
from attr import attr
from attrs import define
from keras import Sequential, Input
from keras.layers import Bidirectional, LSTM, Dropout, BatchNormalization, \
    Dense
from keras.saving.save import load_model
from sklearn.preprocessing import MinMaxScaler
from apps.models.ai.base_model import BaseModel



@define
class ComplexLstmModel(BaseModel, ABC):
    def __attrs_post_init__(self):
        self.model = Sequential([
            Input(shape=(self.sequence_length, self.dimension)),
            Bidirectional(LSTM(512,
                               return_sequences=True,
                               recurrent_activation="sigmoid",
                               use_bias=True,
                               unroll=True)),
            Dropout(0.3),
            BatchNormalization(),
            Bidirectional(LSTM(384,
                               return_sequences=True,
                               recurrent_activation="sigmoid",
                               use_bias=True,
                               unroll=True)),
            Dropout(0.3),
            BatchNormalization(),
            Bidirectional(LSTM(256,
                               return_sequences=True,
                               recurrent_activation="sigmoid",
                               use_bias=True,
                               unroll=True)),
            Dropout(0.3),
            BatchNormalization(),
            Bidirectional(LSTM(128,
                               recurrent_activation="sigmoid",
                               use_bias=True,
                               unroll=True)),
            Dropout(0.3),
            Dense(256, activation="relu"),
            Dense(128, activation="relu"),
            Dense(64, activation="relu"),
            Dense(1)
        ])
        self.model.compile(optimizer="adam", loss="mean_squared_error")
        self.log_model_summary()

    @override
    def train(self,
              training_data,
              target_values,
              epochs: int = 20,
              batch_size: int = 32):
        self.model.fit(training_data,
                       target_values,
                       epochs=epochs,
                       batch_size=batch_size,
                       verbose=1)

    @override
    def predict(self, training_data, target_scaler):
        scaled_pred = self.model.predict(training_data)
        real_price = target_scaler.inverse_transform(scaled_pred)[0][0]
        return real_price

    @override
    def save_model(self,
                   path: str):
        self.model.save(path)

    @override
    def load_model(self,
                   path: str):
        self.model = load_model(path)

    @staticmethod
    @override
    def get_model_path(target_currency: str) -> str:
        from apps.models.model_retriever import COMPLEX_MODEL_DIRECTORY
        return COMPLEX_MODEL_DIRECTORY + target_currency + '_complex_model.keras'