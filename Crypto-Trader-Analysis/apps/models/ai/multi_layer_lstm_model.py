# multi_layer_lstm_model.py
import logging
from abc import ABC
from typing_extensions import override

from attr import attr
from attrs import define

from keras import Input, Model
from keras.layers import LSTM, Dropout, Dense, Concatenate
from keras.saving.save import load_model
from sklearn.preprocessing import MinMaxScaler

from apps.models.ai.multi_layer_base_model import MultiLayerBaseModel



@define
class MultiLayerLstmModel(MultiLayerBaseModel, ABC):
    def __attrs_post_init__(self):
        if self.dimension is None:
            raise ValueError("Please specify 'dimension' for MultiLayerLstmModel.")
        # TODO: Turn repeated behavior into a function.
        short_input = Input(shape=(self.short_seq_len, self.dimension), name="short_input")
        short_lstm = LSTM(150, return_sequences=True, recurrent_activation="sigmoid", use_bias=True, unroll=True)(short_input)
        short_lstm = Dropout(0.2)(short_lstm)
        short_lstm = LSTM(100, recurrent_activation="sigmoid", use_bias=True, unroll=True)(short_lstm)
        short_lstm = Dense(50, activation="relu")(short_lstm)

        medium_input = Input(shape=(self.medium_seq_len, self.dimension), name="medium_input")
        medium_lstm = LSTM(150, return_sequences=True, recurrent_activation="sigmoid", use_bias=True, unroll=True)(medium_input)
        medium_lstm = Dropout(0.2)(medium_lstm)
        medium_lstm = LSTM(100, recurrent_activation="sigmoid", use_bias=True, unroll=True)(medium_lstm)
        medium_lstm = Dense(50, activation="relu")(medium_lstm)

        long_input = Input(shape=(self.long_seq_len, self.dimension), name="long_input")
        long_lstm = LSTM(150, return_sequences=True, recurrent_activation="sigmoid", use_bias=True, unroll=True)(long_input)
        long_lstm = Dropout(0.2)(long_lstm)
        long_lstm = LSTM(100, recurrent_activation="sigmoid", use_bias=True, unroll=True)(long_lstm)
        long_lstm = Dense(50, activation="relu")(long_lstm)
        merged = Concatenate()([short_lstm, medium_lstm, long_lstm])
        merged = Dense(50, activation="relu")(merged)
        final_model = Dense(1)(merged)

        self.model = Model(
            inputs=[short_input, medium_input, long_input],
            outputs=final_model,
            name="MultiLayerLstmModel"
        )
        self.model.compile(optimizer="adam", loss="mean_squared_error")
        self.model.summary(print_fn=lambda x: logging.info(x))

    def train(self, training_data_list, target_values,
              epochs=20, batch_size=32):
        self.model.fit(
            training_data_list,
            target_values,
            epochs=epochs,
            batch_size=batch_size,
            verbose=1
        )

    def predict(self, input_data_list, target_scaler: MinMaxScaler):
        pred_scaled = self.model.predict(input_data_list)
        real_price = target_scaler.inverse_transform(pred_scaled)[0][0]
        return real_price

    def save_model(self, path: str):
        self.model.save(path)

    def load_model(self, path: str):
        self.model = load_model(path)

    @staticmethod
    @override
    def get_model_path(target_currency: str) -> str:
        from apps.models.model_retriever import MULTI_LAYER_MODEL_DIRECTORY
        return MULTI_LAYER_MODEL_DIRECTORY + target_currency + '_multi_layer_model.keras'