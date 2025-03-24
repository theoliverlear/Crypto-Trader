# run_gpu_one.py
# AMD RX 6900 XT
from apps.models.ai.model_type import ModelType
from apps.models.prediction.predictions import predict_and_send_all_loop
from apps.models.train_model import get_split_currency_list, \
    full_train_all_models, setup_tensorflow_env, setup_logging, \
    train_all_models, train_new_models, train_model, train_inaccurate_models
from apps.models.training.training_type import TrainingType
from currency_json_generator import get_all_currency_codes


def main():
    setup_logging()
    setup_tensorflow_env()
    # full_train_all_models(get_all_currency_codes(True),
    #                       gpu_id=1,
    #                       model_type=ModelType.MULTI_LAYER)
    gpu_zero_currencies, gpu_one_currencies = get_split_currency_list()
    # # reverse list
    # gpu_one_currencies.reverse()
    # while True:
    #     train_all_models(training_type=TrainingType.LARGEST_DATA_DETAILED_TRAINING,
    #                      currency_codes=gpu_one_currencies,
    #                      gpu_id=1,
    #                      model_type=ModelType.LSTM)
    # predict_and_send_all_loop()
    # train_new_models(
    #     training_type=TrainingType.LARGEST_DATA_DETAILED_TRAINING,
    #     currency_codes=get_all_currency_codes(True),
    #     gpu_id=1,
    #     model_type=ModelType.LSTM,
    # )
    # train_model(target_currency="ETH",
    #             training_type=TrainingType.LARGEST_DATA_DETAILED_TRAINING,
    #             gpu_id=1,
    #             model_type=ModelType.LSTM)
    train_inaccurate_models(
        training_type=TrainingType.ALL_DATA_DETAILED_TRAINING,
        gpu_id=1,
        model_type=ModelType.LSTM,
    )



if __name__ == "__main__":
    main()