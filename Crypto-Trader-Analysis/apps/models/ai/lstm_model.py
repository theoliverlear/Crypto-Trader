# lstm_model.py
from abc import ABC
from typing_extensions import override
from attrs import define
from keras import Sequential, Input
from keras.layers import LSTM, Dropout, Dense
from keras.saving.save import load_model
from apps.models.ai.base_model import BaseModel

@define
class LstmModel(BaseModel, ABC):
    def __attrs_post_init__(self):
        self.model = Sequential([
            Input(shape=(self.sequence_length, self.dimension)),
            LSTM(150, return_sequences=True, recurrent_activation="sigmoid", use_bias=True, unroll=True),
            Dropout(0.2),
            LSTM(100, recurrent_activation="sigmoid", use_bias=True, unroll=True),
            Dense(50, activation="relu"),
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
        from apps.models.model_retriever import LSTM_MODEL_DIRECTORY
        return LSTM_MODEL_DIRECTORY + target_currency + '_model.keras'