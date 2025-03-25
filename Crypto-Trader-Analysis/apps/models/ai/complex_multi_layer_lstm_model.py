# complex_multi_layer_lstm_model.py
from abc import ABC
from datetime import datetime

from typing_extensions import override

from attrs import define
from keras import Input, Model
from keras.layers import Bidirectional, LSTM, Dropout, BatchNormalization, \
    Concatenate, Dense
from keras.saving.save import load_model

from apps.models.ai.multi_layer_base_model import MultiLayerBaseModel
import os
from keras.callbacks import EarlyStopping, ModelCheckpoint, ReduceLROnPlateau


@define
class ComplexMultiLayerLstmModel(MultiLayerBaseModel, ABC):
    def __attrs_post_init__(self):
        if self.dimension is None:
            raise ValueError(
                "Please specify 'dimension' for MultiLayerLstmModel.")
        # TODO: Turn repeated behavior into a function.
        short_input = Input(shape=(self.short_seq_len, self.dimension),
                            name="short_input")
        medium_input = Input(shape=(self.medium_seq_len, self.dimension),
                             name="medium_input")
        long_input = Input(shape=(self.long_seq_len, self.dimension),
                           name="long_input")

        short_lstm = Bidirectional(LSTM(512,
                                        return_sequences=True,
                                        recurrent_activation="sigmoid",
                                        use_bias=True,
                                        unroll=True))(short_input)
        short_lstm = Dropout(0.3)(short_lstm)
        short_lstm = BatchNormalization()(short_lstm)

        short_lstm = Bidirectional(LSTM(384,
                                        return_sequences=True,
                                        recurrent_activation="sigmoid",
                                        use_bias=True,
                                        unroll=True))(short_lstm)
        short_lstm = Dropout(0.3)(short_lstm)
        short_lstm = BatchNormalization()(short_lstm)

        short_lstm = Bidirectional(LSTM(256,
                                        return_sequences=True,
                                        recurrent_activation="sigmoid",
                                        use_bias=True,
                                        unroll=True))(short_lstm)
        short_lstm = Dropout(0.3)(short_lstm)
        short_lstm = BatchNormalization()(short_lstm)

        short_lstm = Bidirectional(LSTM(128,
                                        recurrent_activation="sigmoid",
                                        use_bias=True,
                                        unroll=True))(short_lstm)
        short_lstm = Dropout(0.3)(short_lstm)

        medium_lstm = Bidirectional(LSTM(512,
                                         return_sequences=True,
                                         recurrent_activation="sigmoid",
                                         use_bias=True, unroll=True))(medium_input)
        medium_lstm = Dropout(0.3)(medium_lstm)
        medium_lstm = BatchNormalization()(medium_lstm)

        medium_lstm = Bidirectional(LSTM(384,
                                         return_sequences=True,
                                         recurrent_activation="sigmoid",
                                         use_bias=True,
                                         unroll=True))(medium_lstm)
        medium_lstm = Dropout(0.3)(medium_lstm)
        medium_lstm = BatchNormalization()(medium_lstm)

        medium_lstm = Bidirectional(LSTM(256,
                                         return_sequences=True,
                                         recurrent_activation="sigmoid",
                                         use_bias=True,
                                         unroll=True))(medium_lstm)
        medium_lstm = Dropout(0.3)(medium_lstm)
        medium_lstm = BatchNormalization()(medium_lstm)

        medium_lstm = Bidirectional(LSTM(128,
                                         recurrent_activation="sigmoid",
                                         use_bias=True,
                                         unroll=True))(medium_lstm)
        medium_lstm = Dropout(0.3)(medium_lstm)

        long_lstm = Bidirectional(LSTM(512,
                                       return_sequences=True,
                                       recurrent_activation="sigmoid",
                                       use_bias=True,
                                       unroll=True))(long_input)
        long_lstm = Dropout(0.3)(long_lstm)
        long_lstm = BatchNormalization()(long_lstm)

        long_lstm = Bidirectional(LSTM(384,
                                       return_sequences=True,
                                       recurrent_activation="sigmoid",
                                       use_bias=True,
                                       unroll=True))(long_lstm)
        long_lstm = Dropout(0.3)(long_lstm)
        long_lstm = BatchNormalization()(long_lstm)

        long_lstm = Bidirectional(LSTM(256,
                                       return_sequences=True,
                                       recurrent_activation="sigmoid",
                                       use_bias=True,
                                       unroll=True))(long_lstm)
        long_lstm = Dropout(0.3)(long_lstm)
        long_lstm = BatchNormalization()(long_lstm)

        long_lstm = Bidirectional(LSTM(128,
                                       recurrent_activation="sigmoid",
                                       use_bias=True,
                                       unroll=True))(long_lstm)
        long_lstm = Dropout(0.3)(long_lstm)

        merged = Concatenate()([short_lstm, medium_lstm, long_lstm])

        merged = Dense(256, activation="relu")(merged)
        merged = Dense(128, activation="relu")(merged)
        merged = Dense(64, activation="relu")(merged)
        final_model = Dense(1)(merged)
        self.model = Model(inputs=[short_input, medium_input, long_input],
                           outputs=final_model,
                           name="MultiLayerLSTM")
        self.model.compile(optimizer="adam", loss="mean_squared_error")
        self.log_model_summary()


    @override
    @staticmethod
    def get_tensorboard_callback(target_currency: str):
        import tensorflow as tf
        timestamp = datetime.now().strftime("%Y%m%d-%H%M%S")
        log_dir = os.path.join("logs", "COMPLEX_MULTI_LAYER_LSTM", target_currency, timestamp)

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
        from apps.models.ai.model_retriever import \
            COMPLEX_MUlTI_LAYER_MODEL_DIRECTORY
        checkpoint_dir = os.path.join(COMPLEX_MUlTI_LAYER_MODEL_DIRECTORY,
                                      "checkpoints")
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
            ComplexMultiLayerLstmModel.get_tensorboard_callback(self.target_currency)
        ]
        self.model.fit(dataset,
                       epochs=epochs,
                       batch_size=batch_size,
                       verbose=1,
                       callbacks=callbacks)

    @override
    def predict(self, input_data_list, target_scaler):
        scaled_pred = self.model.predict(input_data_list)
        real_price = target_scaler.inverse_transform(scaled_pred)[0][0]
        return real_price

    @override
    def save_model(self, path: str):
        self.model.save(path)

    @override
    def load_model(self, path: str):
        self.model = load_model(path)

    @staticmethod
    @override
    def get_model_path(target_currency: str) -> str:
        from apps.models.ai.model_retriever import \
            COMPLEX_MUlTI_LAYER_MODEL_DIRECTORY
        return COMPLEX_MUlTI_LAYER_MODEL_DIRECTORY + target_currency + '_complex_multi_layer_model.keras'