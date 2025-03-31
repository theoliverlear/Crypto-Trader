# train_model.py
import logging
import os

os.environ["TF_XLA_FLAGS"] = "--tf_xla_auto_jit=2"
os.environ["TF_CPP_MIN_LOG_LEVEL"] = "3"
os.environ["TF_ENABLE_ONEDNN_OPTS"] = "1"

import tensorflow as tf
tf.config.optimizer.set_jit(True)
tf.get_logger().setLevel('ERROR')

from concurrent.futures import ThreadPoolExecutor

import numpy as np
from pandas import DataFrame
from sklearn.preprocessing import MinMaxScaler

from apps.models.ai.complex_lstm_model import ComplexLstmModel
from apps.models.ai.lstm_model import LstmModel
from apps.models.ai.model_type import ModelType
from apps.models.data.preprocessor import Preprocessor
from apps.models.database.query_type import QueryType
from apps.models.ai.model_retriever import get_model, get_lstm_model, \
    get_complex_lstm_model
from apps.models.training.training_type import TrainingType
from currency_json_generator import get_all_currency_codes

# num_cores = str(os.cpu_count())
# os.environ["OMP_NUM_THREADS"] = num_cores
# os.environ["TF_NUM_INTRAOP_THREADS"] = num_cores
# os.environ["TF_NUM_INTEROP_THREADS"] = num_cores

import tensorflow as tf
tf.config.optimizer.set_jit(True)
tf.function(jit_compile=True)

def setup_logging():
    logging.basicConfig(
        level=logging.DEBUG,
        format='[%(asctime)s] [%(levelname)s] %(message)s',
        datefmt='%Y-%m-%d %H:%M:%S'
    )
    logging.debug("Logging setup complete: Debug mode is enabled.")

def configure_concurrency():
    # num_cores = str(os.cpu_count())
    # os.environ["OMP_NUM_THREADS"] = num_cores
    # os.environ["TF_NUM_INTRAOP_THREADS"] = num_cores
    # os.environ["TF_NUM_INTEROP_THREADS"] = num_cores
    os.environ["TF_XLA_FLAGS"] = "--tf_xla_auto_jit=2"
    os.environ["TF_CPP_MIN_LOG_LEVEL"] = "3"
    os.environ["TF_ENABLE_ONEDNN_OPTS"] = "1"

def setup_tensorflow_env():
    if tf.config.optimizer.get_jit():
        print("XLA JIT is enabled")
    else:
        print("XLA JIT is NOT enabled")


    # num_cores: int = os.cpu_count()
    # tf.config.threading.set_inter_op_parallelism_threads(num_cores)
    # tf.config.threading.set_intra_op_parallelism_threads(num_cores)
    # tf.config.optimizer.set_jit(True)
    # tf.function(jit_compile=True)
    gpus = tf.config.list_physical_devices('GPU')
    dmls = tf.config.list_physical_devices('DML')
    if gpus:
        logging.info(f"{len(gpus)} GPU(s) detected.")
        for index, pc_gpu in enumerate(gpus):
            # logging.info(f"Configuring GPU {pc_gpu} for memory growth.")
            # memory: int = 10000 if index == 1 else 14000
            # tf.config.experimental.set_virtual_device_configuration(
            #     pc_gpu,
            #     [tf.config.experimental.VirtualDeviceConfiguration(memory_limit=memory)]
            # )
            tf.config.experimental.set_memory_growth(pc_gpu, True)
    if dmls:
        logging.info(f"{len(dmls)} DML(s) detected.")
        for index, pc_dml in enumerate(dmls):
            details = tf.config.experimental.get_device_details(pc_dml)
            logging.info(f"Configuring DML {pc_dml} with details: {details}")
            tf.config.experimental.set_memory_growth(pc_dml, True)
    else:
        logging.info("No DML devices detected.")
    if not gpus and not dmls:
        logging.warning("No GPU or DML devices detected. Running on CPU.")
    logging.info(f"TensorFlow Configured for CPU Cores and {len(gpus)} GPU(s).")


def get_untrained_models() -> list[str]:
    from apps.models.prediction.predictions import model_exists
    currencies: list[str] = get_all_currency_codes(False)
    untrained_models: list[str] = []
    for currency in currencies:
        if not model_exists(currency):
            untrained_models.append(currency)
    return untrained_models

def train_new_models(currency_codes: list[str] = None,
                     training_type: TrainingType = TrainingType.BALANCED_MEDIUM_TRAINING,
                     model_type: ModelType = ModelType.LSTM,
                     gpu_id: int = 0):
    # from apps.models.prediction.predictions import model_exists
    # for currency in currency_codes:
    #     if not model_exists(currency):
    #         logging.info(f"No model found for {currency}. Training model now...")
    #         train_model(currency, training_type, model_type=model_type, gpu_id=gpu_id, use_previous_model=False)
    #         logging.info(f"Training completed for {currency}.")
    #     else:
    #         logging.debug(f"Model already exists for {currency}. Skipping.")
    for currency in get_untrained_models():
        if currency_codes:
            if currency in currency_codes:
                logging.info(f"Training model for {currency}...")
                train_model(target_currency=currency,
                            training_type=training_type,
                            model_type=model_type,
                            gpu_id=gpu_id)
                logging.info(f"Model for {currency} trained and saved.")
        else:
            logging.info(f"Training model for {currency}...")
            train_model(target_currency=currency,
                        training_type=training_type,
                        model_type=model_type,
                        gpu_id=gpu_id)
            logging.info(f"Model for {currency} trained and saved.")


def train_inaccurate_models(training_type: TrainingType = TrainingType.BALANCED_MEDIUM_TRAINING,
                            model_type: ModelType = ModelType.LSTM,
                            gpu_id: int = 0,
                            only_recent_predictions: bool = False):
    from apps.models.database.database import Database
    db: Database = Database()
    inaccurate_models: list[str] = db.get_inaccurate_models(5, only_recent_predictions)
    if gpu_id == 1:
        inaccurate_models.reverse()
    logging.info(f"Found {len(inaccurate_models)} inaccurate models.")

    for currency in inaccurate_models:
        logging.info(f"Retraining model for {currency}...")
        train_model(target_currency=currency,
                    training_type=training_type,
                    model_type=model_type,
                    gpu_id=gpu_id)
        logging.info(f"Model for {currency} retrained and saved.")

def get_data_frame(target_currency: str = 'BTC', limit: int = 2000, query_type=QueryType.HISTORICAL_PRICE) -> DataFrame:
    from apps.models.database.database import Database
    logging.info("Creating database connection...")
    db: Database = Database()
    logging.info("Fetching data frame...")
    df: DataFrame = db.fetch_data(target_currency, limit, query_type)
    logging.info("Handling price columns...")
    df.rename(columns={"target_price": f"{target_currency}_price"}, inplace=True)
    return df

def get_last_historical_price(historical_prices):
    return np.array([historical_prices[-1]])

def get_last_currency_price(dataframe: DataFrame, target_currency: str):
    return dataframe[f"{target_currency}_price"].iloc[-1]

def train_model(target_currency: str = 'BTC',
                training_type: TrainingType=TrainingType.BALANCED_MEDIUM_TRAINING,
                model_type: ModelType=ModelType.LSTM,
                gpu_id: int = 0,
                use_previous_model: bool = True):
    from apps.models.prediction.predictions import log_actual_vs_printed
    configure_concurrency()
    logging.info("Getting data frame...")
    dataframe = get_data_frame(target_currency, limit=training_type.value.max_rows, query_type=QueryType.HISTORICAL_PRICE)
    logging.debug(f"Retrieved {len(dataframe)} rows for {target_currency}")
    if len(dataframe) < 50 and training_type.value.skip_small_samples:
        logging.warning(f"Not enough data to train model for {target_currency}. Skipping...")
        return
    logging.info("Configuring preprocessor...")
    preprocessor = Preprocessor()
    logging.info("Transforming data...")
    historical_prices, future_prices_unscaled, input_scaler = preprocessor.transform(dataframe, target_currency)
    logging.info("Dropping NaN values...")
    dataframe.dropna(subset=[f"{target_currency}_price"], inplace=True)
    logging.info("Scaling target values...")
    target_scaler = MinMaxScaler(feature_range=(0,1))
    logging.info("Getting raw target values...")
    raw_target_vals = get_currency_prices(dataframe, target_currency)
    logging.info("Fitting target scaler...")
    target_scaler.fit(raw_target_vals)
    logging.info("Scaling future prices...")
    future_prices_scaled = target_scaler.transform(rescale_future_prices(future_prices_unscaled)).ravel()
    dataset = tf.data.Dataset.from_tensor_slices(
        (historical_prices, future_prices_scaled))
    dataset = dataset.cache().shuffle(1024).batch(
        training_type.value.batch_size).prefetch(tf.data.AUTOTUNE)


    logging.info(
        f"Preprocessing Completed! historical_prices shape: {historical_prices.shape}, "
        f"future_prices shape: {future_prices_unscaled.shape}"
    )
    if model_type == ModelType.LSTM:
        model_path: str = LstmModel.get_model_path(target_currency)
    elif model_type == ModelType.COMPLEX_LSTM:
        model_path: str = ComplexLstmModel.get_model_path(target_currency)
    else:
        raise ValueError(f"Incompatible model type: {model_type}")
    logging.info(f"Getting model for {target_currency} at {model_path}...")
    if model_type == ModelType.LSTM:
        model: LstmModel = get_lstm_model(target_currency, model_path, historical_prices, training_type, use_previous_model)
    elif model_type == ModelType.COMPLEX_LSTM:
        model: ComplexLstmModel = get_complex_lstm_model(target_currency, model_path, historical_prices, training_type, use_previous_model)
    else:
        raise ValueError(f"Incompatible model type: {model_type}")
    logging.info(f"Training model for {target_currency}...")
    with tf.device(f'/GPU:{gpu_id}'):
        logging.info(f"Training on GPU...")
        # model.train(historical_prices, future_prices_scaled, epochs=training_type.value.epochs, batch_size=training_type.value.epochs)
        model.train(dataset, epochs=training_type.value.epochs, batch_size=training_type.value.epochs)
    logging.info(f"Training completed for {target_currency}.")
    logging.info(f"Saving model for {target_currency}...")
    model.save_model(model_path)
    logging.info(f"Getting last sequence.")
    last_sequence = get_last_historical_price(historical_prices)
    logging.info(f"Predicting currency price.")
    predicted_price = model.predict(last_sequence, target_scaler)
    logging.debug(f"Predicted Next {target_currency} Price: {predicted_price}")
    log_actual_vs_printed(target_currency, predicted_price, model_type)

def get_currency_prices(dataframe, target_currency):
    return dataframe[[f"{target_currency}_price"]].values

def rescale_future_prices(future_prices_unscaled):
    return future_prices_unscaled.reshape(-1, 1)

def train_all_models(training_type: TrainingType = TrainingType.BALANCED_MEDIUM_TRAINING,
                     currency_codes: list[str] = None,
                     model_type: ModelType = ModelType.LSTM,
                     gpu_id: int = 0):
    for currency_code in currency_codes:
        logging.debug(f"Training model for {currency_code}...")
        if model_type == ModelType.LSTM or model_type == ModelType.COMPLEX_LSTM:
            train_model(currency_code, training_type, gpu_id=gpu_id, model_type=model_type)
        elif model_type == ModelType.MULTI_LAYER or model_type == ModelType.COMPLEX_MULTI_LAYER:
            train_multi_layer_model(currency_code,
                                    10,
                                    50,
                                    150,
                                    training_type,
                                    model_type,
                                    gpu_id)
        logging.debug(f"Model for {currency_code} trained successfully.")

def full_train_model(target_currency: str = 'BTC', gpu_id: int = 0, model_type: ModelType = ModelType.LSTM):
    for training_type in TrainingType:
        logging.debug(f"Training model for {target_currency} with training type {training_type}...")
        train_model(target_currency, training_type, gpu_id=gpu_id, model_type=model_type)
        logging.debug(f"Model for {target_currency} trained successfully with training type {training_type}.")
    train_model(target_currency, TrainingType.LARGE_DATA_DETAILED_TRAINING)

def full_train_layered_model(target_currency: str = 'BTC',
                             short_seq: int = 10,
                             medium_seq: int = 50,
                             long_seq: int = 150,
                             model_type: ModelType = ModelType.MULTI_LAYER,
                             gpu_id: int = 0):
    for training_type in TrainingType:
        logging.debug(f"Training model for {target_currency} with training type {training_type}...")
        train_multi_layer_model(target_currency, short_seq, medium_seq, long_seq, training_type, model_type, gpu_id)
        logging.debug(f"Model for {target_currency} trained successfully with training type {training_type}.")
    train_multi_layer_model(target_currency,
                            short_seq,
                            medium_seq,
                            long_seq,
                            TrainingType.LARGE_DATA_DETAILED_TRAINING,
                            model_type,
                            gpu_id)

def full_train_all_models(currency_codes: list[str] = None,
                          gpu_id: int = 0,
                          model_type: ModelType = ModelType.LSTM):
    for currency_code in currency_codes:
        if model_type == ModelType.LSTM or model_type == ModelType.COMPLEX_LSTM:
            logging.debug(f"Training model for {currency_code}...")
            full_train_model(currency_code, gpu_id=gpu_id, model_type=model_type)
            logging.debug(f"Model for {currency_code} trained successfully.")
        elif model_type == ModelType.MULTI_LAYER or model_type == ModelType.COMPLEX_MULTI_LAYER:
            logging.debug(f"Training multi-layer model for {currency_code}...")
            full_train_layered_model(currency_code, gpu_id=gpu_id, model_type=model_type)
            logging.debug(f"Multi-layer model for {currency_code} trained successfully.")

def train_multi_layer_model(target_currency: str = 'BTC',
                            short_seq: int = 10,
                            medium_seq: int = 50,
                            long_seq: int = 150,
                            training_type: TrainingType = TrainingType.BALANCED_MEDIUM_TRAINING,
                            model_type: ModelType = ModelType.MULTI_LAYER,
                            gpu_id: int = 0):
    from apps.models.prediction.predictions import log_actual_vs_printed
    from apps.models.ai.multi_layer_lstm_model import MultiLayerLstmModel
    from apps.models.ai.complex_multi_layer_lstm_model import ComplexMultiLayerLstmModel

    logging.info(f"Fetching data frame for {target_currency}")
    dataframe = get_data_frame(target_currency,
                               limit=training_type.value.max_rows,
                               query_type=QueryType.HISTORICAL_PRICE_SPACED)
    if dataframe.empty:
        logging.warning(f"No data for {target_currency}. Cannot train multi-layer model.")
        return
    logging.debug(f"Dataframe has {len(dataframe)} rows for {target_currency}.")
    preprocessor = Preprocessor()
    short_data, med_data, long_data, future_prices, input_scaler = \
        preprocessor.transform_multi_scale_with_weights(
            dataframe,
            target_currency,
            short_seq=short_seq,
            medium_seq=medium_seq,
            long_seq=long_seq
        )
    if short_data.shape[0] == 0:
        logging.warning(f"No samples produced for {target_currency} in multi-scale transform.")
        return
    target_scaler = MinMaxScaler(feature_range=(0,1))
    future_prices_scaled = target_scaler.fit_transform(future_prices.reshape(-1,1)).ravel()

    if model_type == ModelType.MULTI_LAYER:
        model_path = MultiLayerLstmModel.get_model_path(target_currency)
        model_class = MultiLayerLstmModel
    elif model_type == ModelType.COMPLEX_MULTI_LAYER:
        model_path = ComplexMultiLayerLstmModel.get_model_path(target_currency)
        model_class = ComplexMultiLayerLstmModel
    else:
        raise ValueError(f"Incompatible model type: {model_type}")

    dataset = tf.data.Dataset.from_tensor_slices((
        (short_data, med_data, long_data),
        future_prices_scaled
    ))
    dataset = dataset.cache().shuffle(1024) \
        .batch(training_type.value.batch_size) \
        .prefetch(tf.data.AUTOTUNE)

    model = get_model(model_class,
                      target_currency,
                      model_path,
                      short_data,
                      training_type)

    logging.info(f"Training Multi-Layer Model for {target_currency}...")
    with tf.device(f'/GPU:{gpu_id}'):
        logging.info(f"Training on GPU...")
        model.train(
            dataset,
            epochs=training_type.value.epochs,
            batch_size=training_type.value.batch_size
        )

    logging.info(f"Saving Multi-Layer Model to {model_path}")
    model.save_model(model_path)
    last_long, last_medium, last_short = get_last_multi_layer_elements(long_data, med_data, short_data)
    predicted_price = model.predict([last_short, last_medium, last_long],
                                    target_scaler=target_scaler)
    log_actual_vs_printed(target_currency, predicted_price)

def get_last_multi_layer_elements(long_data, med_data, short_data):
    last_short = np.array([short_data[-1]])
    last_medium = np.array([med_data[-1]])
    last_long = np.array([long_data[-1]])
    return last_long, last_medium, last_short

def train_all_models_dual_gpu(model_type: ModelType = ModelType.LSTM):
    gpu_zero_currencies, gpu_one_currencies = get_split_currency_list()
    def train_on_gpu(currencies, gpu_id):
        full_train_all_models(currencies, gpu_id=gpu_id, model_type=model_type)

    with ThreadPoolExecutor(max_workers=2) as executor:
        executor.submit(train_on_gpu, gpu_zero_currencies, 0)
        executor.submit(train_on_gpu, gpu_one_currencies, 1)

def get_split_currency_list():
    currency_codes = get_all_currency_codes(False)
    mid_index = len(currency_codes) // 2
    gpu_zero_currencies = currency_codes[:mid_index]
    gpu_one_currencies = currency_codes[mid_index:]
    return gpu_zero_currencies, gpu_one_currencies

def main():
    setup_logging()
    setup_tensorflow_env()

if __name__ == "__main__":
    main()