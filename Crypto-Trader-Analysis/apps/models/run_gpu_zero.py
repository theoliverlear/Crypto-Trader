# run_gpu_zero.py
# AMD RX 6700 XT
from apps.models.ai.model_type import ModelType
from apps.models.train_model import get_split_currency_list, \
    full_train_all_models, setup_tensorflow_env, setup_logging, \
    train_all_models, train_new_models, train_multi_layer_model, train_model
from apps.models.training.training_type import TrainingType
from currency_json_generator import get_all_currency_codes


def main():
    setup_logging()
    setup_tensorflow_env()
    gpu_zero_currencies, gpu_one_currencies = get_split_currency_list()
    # reverse list
    # gpu_zero_currencies.reverse()
    # while True:
    #     train_all_models(training_type=TrainingType.LARGEST_DATA_DETAILED_TRAINING,
    #                      currency_codes=gpu_zero_currencies,
    #                      gpu_id=0,
    #                      model_type=ModelType.LSTM)
    # train_new_models(
    #     training_type=TrainingType.LARGEST_DATA_DETAILED_TRAINING,
    #     currency_codes=gpu_zero_currencies,
    #     gpu_id=0,
    #     model_type=ModelType.LSTM
    # )
    # train_multi_layer_model(
    #     training_type=TrainingType.RECENT_BALANCED_MEDIUM_TRAINING,
    #     target_currency="DOGE",
    #     gpu_id=0,
    #     model_type=ModelType.COMPLEX_MULTI_LAYER
    # )
    # train_model(
    #     target_currency="DOGE",
    #     training_type=TrainingType.RECENT_DETAILED_SHORT_TRAINING,
    #     gpu_id=0,
    #     model_type=ModelType.LSTM
    # )
    # train_model(
    #     target_currency="DOGE",
    #     training_type=TrainingType.RECENT_DETAILED_SHORT_TRAINING,
    #     gpu_id=0,
    #     model_type=ModelType.COMPLEX_LSTM
    # )
    # train_multi_layer_model(
    #     target_currency="DOGE",
    #     training_type=TrainingType.RECENT_DETAILED_SHORT_TRAINING,
    #     gpu_id=0,
    #     model_type=ModelType.MULTI_LAYER
    # )
    # train_multi_layer_model(
    #     target_currency="DOGE",
    #     training_type=TrainingType.RECENT_DETAILED_SHORT_TRAINING,
    #     gpu_id=0,
    #     model_type=ModelType.COMPLEX_MULTI_LAYER
    # )
    train_new_models(
        training_type=TrainingType.ALL_DATA_DETAILED_TRAINING,
        currency_codes=get_all_currency_codes(True),
        gpu_id=1,
        model_type=ModelType.LSTM,
    )

if __name__ == "__main__":
    main()