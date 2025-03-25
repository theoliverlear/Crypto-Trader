# run_gpu_zero.py
# AMD RX 6700 XT
from apps.models.ai.model_type import ModelType
from apps.models.training.train_model import get_split_currency_list, \
    setup_tensorflow_env, setup_logging, \
    train_all_models
from apps.models.training.training_type import TrainingType


def main():
    setup_logging()
    setup_tensorflow_env()
    gpu_zero_currencies, gpu_one_currencies = get_split_currency_list()

    while True:
        train_all_models(
            training_type=TrainingType.MODERATE_DATA_DETAILED_TRAINING,
            currency_codes=gpu_zero_currencies,
            gpu_id=0,
            model_type=ModelType.COMPLEX_LSTM,
        )

if __name__ == "__main__":
    main()