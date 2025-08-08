# run_gpu_one.py
# AMD RX 6900 XT
from apps.models.ai.model_type import ModelType
from apps.models.database.query_type import QueryType
from apps.models.training.specs.batch_size_evaluations import \
    BatchSizeEvaluations
from apps.models.training.specs.dataset_size import DatasetSize
from apps.models.training.specs.epoch_focus import EpochFocus
from apps.models.training.specs.sequence_length_sentiment import \
    SequenceLengthSentiment
from apps.models.training.train_model import get_split_currency_list, \
    setup_tensorflow_env, setup_logging, \
    train_all_models, train_new_models
from apps.models.training.training_model import TrainingModel
from apps.models.training.training_session import TrainingSession
from apps.models.training.training_type import TrainingType
from currency_json_generator import get_all_currency_codes


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