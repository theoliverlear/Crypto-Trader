# model_retriever.py
import logging
import os
from typing import TypeVar

from apps.models.ai.complex_lstm_model import ComplexLstmModel
from apps.models.ai.complex_multi_layer_lstm_model import \
    ComplexMultiLayerLstmModel
from apps.models.ai.lstm_model import LstmModel
from apps.models.ai.multi_layer_lstm_model import MultiLayerLstmModel
from apps.models.training.training_type import TrainingType

BASE_DIRECTORY = 'models/'

LSTM_MODEL_DIRECTORY = BASE_DIRECTORY + 'lstm_models/'
COMPLEX_MODEL_DIRECTORY = BASE_DIRECTORY + 'complex_lstm_models/'
MULTI_LAYER_MODEL_DIRECTORY = BASE_DIRECTORY + 'multi_layer_models/'
COMPLEX_MUlTI_LAYER_MODEL_DIRECTORY = BASE_DIRECTORY + 'complex_multi_layer_models/'

T = TypeVar('T')

def get_model(model_type: T,
              model_target_currency: str,
              currency_model_path: str,
              historical_prices,
              training_model: TrainingType,
              use_previous_model: bool = True) -> T:
    if os.path.exists(currency_model_path) and use_previous_model:
        logging.info(f"Found existing model: {currency_model_path}. Loading and continuing training...")
        model = model_type(dimension=historical_prices.shape[2],
                           target_currency=model_target_currency,
                           sequence_length=training_model.value.sequence_length)
        model.load_model(currency_model_path)
        return model
    else:
        logging.info(f"No existing model found. Creating a new model for {model_target_currency}...")
        return model_type(dimension=historical_prices.shape[2],
                          target_currency=model_target_currency,
                          sequence_length=training_model.value.sequence_length)

def get_lstm_model(model_target_currency: str,
                   currency_model_path: str,
                   historical_prices,
                   training_model: TrainingType,
                   use_previous_model: bool = True) -> LstmModel:
    if os.path.exists(currency_model_path) and use_previous_model:
        logging.info(f"Found existing model: {currency_model_path}. Loading and continuing training...")
        lstm_model = LstmModel(dimension=historical_prices.shape[2],
                               target_currency=model_target_currency,
                               sequence_length=training_model.value.sequence_length)
        lstm_model.load_model(currency_model_path)
        return lstm_model
    else:
        logging.info(f"No existing model found. Creating a new model for {model_target_currency}...")
        return LstmModel(dimension=historical_prices.shape[2],
                         target_currency=model_target_currency)

def get_complex_lstm_model(model_target_currency: str,
                           currency_model_path: str,
                           historical_prices,
                           training_model: TrainingType,
                           use_previous_model: bool = True) -> ComplexLstmModel:
    if os.path.exists(currency_model_path) and use_previous_model:
        logging.info(f"Found existing model: {currency_model_path}. Loading and continuing training...")
        complex_lstm_model = ComplexLstmModel(dimension=historical_prices.shape[2],
                                              target_currency=model_target_currency,
                                              sequence_length=training_model.value.sequence_length)
        complex_lstm_model.load_model(currency_model_path)
        return complex_lstm_model
    else:
        logging.info(f"No existing model found. Creating a new model for {model_target_currency}...")
        return ComplexLstmModel(dimension=historical_prices.shape[2],
                                target_currency=model_target_currency)

def get_multi_layer_lstm_model(model_target_currency: str,
                               currency_model_path: str,
                               historical_prices,
                               training_model: TrainingType,
                               use_previous_model: bool = True) -> MultiLayerLstmModel:
    if os.path.exists(currency_model_path) and use_previous_model:
        logging.info(f"Found existing model: {currency_model_path}. Loading and continuing training...")
        complex_lstm_model = MultiLayerLstmModel(dimension=historical_prices.shape[2],
                                                 target_currency=model_target_currency,
                                                 sequence_length=training_model.value.sequence_length)
        complex_lstm_model.load_model(currency_model_path)
        return complex_lstm_model
    else:
        logging.info(f"No existing model found. Creating a new model for {model_target_currency}...")
        return MultiLayerLstmModel(dimension=historical_prices.shape[2],
                                   target_currency=model_target_currency,
                                   sequence_length=training_model.value.sequence_length)

def get_complex_multi_layer_lstm_model(model_target_currency: str,
                                       currency_model_path: str,
                                       historical_prices,
                                       training_model: TrainingType,
                                       use_previous_model: bool = True) -> ComplexMultiLayerLstmModel:
    if os.path.exists(currency_model_path) and use_previous_model:
        logging.info(f"Found existing model: {currency_model_path}. Loading and continuing training...")
        complex_lstm_model = ComplexMultiLayerLstmModel(dimension=historical_prices.shape[2],
                                              target_currency=model_target_currency,
                                              sequence_length=training_model.value.sequence_length)
        complex_lstm_model.load_model(currency_model_path)
        return complex_lstm_model
    else:
        logging.info(f"No existing model found. Creating a new model for {model_target_currency}...")
        return ComplexMultiLayerLstmModel(dimension=historical_prices.shape[2],
                                          target_currency=model_target_currency,
                                          sequence_length=training_model.value.sequence_length)