# lstm_model.py
from abc import ABC
from typing import override
from attr import attr
from attrs import define
from keras import Sequential, Input
from keras.src.layers import LSTM, Dropout, Dense
from keras.src.saving import load_model
from sklearn.preprocessing import MinMaxScaler
from apps.models.ai.base_model import BaseModel

@define
class LstmModel(BaseModel, ABC):
    dimension = attr(default=None)
    model: Sequential = attr(factory=Sequential)
    scaler: MinMaxScaler = attr(factory=MinMaxScaler)  
    sequence_length: int = attr(default=10)
    target_currency: str = attr(default='BTC')

    def __attrs_post_init__(self):
        self.model = Sequential([
            Input(shape=(self.sequence_length, self.dimension)),
            LSTM(150, return_sequences=True),
            Dropout(0.2),
            LSTM(100),
            Dense(50, activation="relu"),
            Dense(1)  
        ])
        self.model.compile(optimizer="adam", loss="mean_squared_error")

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