# complex_lstm_model.py
from abc import ABC
from datetime import datetime

from typing_extensions import override
from attrs import define
from keras import Sequential, Input
from keras.layers import Bidirectional, LSTM, Dropout, BatchNormalization, \
    Dense
from keras.saving.save import load_model
from apps.models.ai.base_model import BaseModel
import os
from keras.callbacks import EarlyStopping, ModelCheckpoint, ReduceLROnPlateau




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
    @staticmethod
    def get_tensorboard_callback(target_currency: str):
        import tensorflow as tf
        timestamp = datetime.now().strftime("%Y%m%d-%H%M%S")
        log_dir = os.path.join("logs", "COMPLEX_LSTM_MODEL", target_currency, timestamp)

        return tf.keras.callbacks.TensorBoard(
            log_dir=log_dir,
            profile_batch=10,
            histogram_freq=1
        )

    @override
    def train(self,
              dataset,
              epochs: int = 20,
              batch_size: int = 32):
        from apps.models.ai.model_retriever import COMPLEX_MODEL_DIRECTORY
        checkpoint_dir = os.path.join(COMPLEX_MODEL_DIRECTORY, "checkpoints")
        os.makedirs(checkpoint_dir, exist_ok=True)
        checkpoint_path = os.path.join(checkpoint_dir,
                                       f"{self.target_currency}_checkpoint.keras")

        callbacks = [
            EarlyStopping(monitor="loss", patience=5,
                          restore_best_weights=True),
            ModelCheckpoint(filepath=checkpoint_path, monitor="loss",
                            save_best_only=True),
            ReduceLROnPlateau(monitor='loss', factor=0.5, patience=3,
                              min_lr=1e-6),
            ComplexLstmModel.get_tensorboard_callback(self.target_currency)
        ]
        self.model.fit(dataset,
                       epochs=epochs,
                       batch_size=batch_size,
                       verbose=1,
                       callbacks=callbacks)

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
        from apps.models.ai.model_retriever import COMPLEX_MODEL_DIRECTORY
        return COMPLEX_MODEL_DIRECTORY + target_currency + '_complex_model.keras'