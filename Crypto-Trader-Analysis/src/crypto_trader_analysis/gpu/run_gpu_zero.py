# run_gpu_zero.py
# AMD RX 6700 XT
from src.crypto_trader_analysis.gpu.gpu_yml_reader import train_from_config


def main():
    train_from_config(0)


if __name__ == "__main__":
    main()