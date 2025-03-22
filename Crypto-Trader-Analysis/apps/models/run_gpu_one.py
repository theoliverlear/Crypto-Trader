from apps.models.ai.model_type import ModelType
from apps.models.train_model import get_split_currency_list, \
    full_train_all_models, setup_tensorflow_env, setup_logging, \
    train_all_models
from apps.models.training.training_type import TrainingType
from currency_json_generator import get_all_currency_codes


def main():
    setup_logging()
    setup_tensorflow_env()
    # full_train_all_models(get_all_currency_codes(True),
    #                       gpu_id=1,
    #                       model_type=ModelType.MULTI_LAYER)
    gpu_zero_currencies, gpu_one_currencies = get_split_currency_list()
    while True:
        train_all_models(training_type=TrainingType.LARGEST_DATA_DETAILED_TRAINING,
                     currency_codes=gpu_one_currencies,
                     gpu_id=1,
                     model_type=ModelType.SIMPLE)

if __name__ == "__main__":
    main()