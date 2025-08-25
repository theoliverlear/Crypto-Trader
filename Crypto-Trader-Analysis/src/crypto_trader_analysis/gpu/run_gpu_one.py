# run_gpu_one.py
# AMD RX 6900 XT
from src.crypto_trader_analysis.apps.learning.models.ai.model_type import ModelType
from src.crypto_trader_analysis.apps.learning.models.database.query_type import QueryType
from src.crypto_trader_analysis.apps.learning.models.training.specs.batch_size_evaluations import \
    BatchSizeEvaluations
from src.crypto_trader_analysis.apps.learning.models.training.specs.dataset_size import DatasetSize
from src.crypto_trader_analysis.apps.learning.models.training.specs.epoch_focus import EpochFocus
from src.crypto_trader_analysis.apps.learning.models.training.specs.sequence_length_sentiment import \
    SequenceLengthSentiment
from src.crypto_trader_analysis.apps.learning.models.training.train_model import setup_tensorflow_env, \
    setup_logging, get_split_currency_list
from src.crypto_trader_analysis.apps.learning.models.training.training_model import TrainingModel
from src.crypto_trader_analysis.apps.learning.models.training.training_session import TrainingSession
from src.crypto_trader_analysis.apps.learning.models.currency_json_generator import get_all_currency_codes


def main():
    setup_logging()
    setup_tensorflow_env()
    gpu_zero_currencies, gpu_one_currencies = get_split_currency_list()
    currencies: list[str] = get_all_currency_codes(True)
    important_currencies: list[str] = ["BTC", "ETH", "LTC", "XLM", "DOGE", "SOL", "ADA"]
    currencies.reverse()

    training_model: TrainingModel = TrainingModel().builder() \
                                                   .max_rows(DatasetSize.MASSIVE) \
                                                   .epochs(EpochFocus.COMPLETE_ANALYSIS) \
                                                   .batch_size(BatchSizeEvaluations.GENERALIZED) \
                                                   .sequence_length(SequenceLengthSentiment.get_by_time(days=1)) \
                                                   .query_type(QueryType.HISTORICAL_PRICE) \
                                                   .build()
    training_session: TrainingSession = TrainingSession(
        gpu_id=1,
        training_model=training_model,
        target_currency='BTC',
        model_type=ModelType.LSTM
    )
    training_session.train()



if __name__ == "__main__":
    main()