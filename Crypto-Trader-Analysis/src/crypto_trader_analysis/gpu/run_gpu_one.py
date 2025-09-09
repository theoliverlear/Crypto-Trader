# run_gpu_one.py
# AMD RX 6900 XT
from src.crypto_trader_analysis.gpu.gpu_yml_reader import train_from_config


def main():
    train_from_config(1)


if __name__ == "__main__":
    main()