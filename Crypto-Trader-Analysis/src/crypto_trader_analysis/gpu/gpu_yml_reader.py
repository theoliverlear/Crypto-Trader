from __future__ import annotations

import os
from typing import Any, Dict, List

try:
    import yaml  
except Exception as e:  
    yaml = None

from src.crypto_trader_analysis.apps.learning.models.ai.model_type import ModelType
from src.crypto_trader_analysis.apps.learning.models.database.query_type import QueryType
from src.crypto_trader_analysis.apps.learning.models.training.specs.batch_size_evaluations import \
    BatchSizeEvaluations
from src.crypto_trader_analysis.apps.learning.models.training.specs.dataset_size import DatasetSize
from src.crypto_trader_analysis.apps.learning.models.training.specs.epoch_focus import EpochFocus
from src.crypto_trader_analysis.apps.learning.models.training.training_model import TrainingModel
from src.crypto_trader_analysis.apps.learning.models.training.training_session import TrainingSession
from src.crypto_trader_analysis.apps.learning.models.training.train_model import setup_logging, setup_tensorflow_env, \
    get_split_currency_list
from src.crypto_trader_analysis.apps.learning.models.currency_json_generator import get_all_currency_codes



_ANALYSIS_DIR = os.path.dirname(os.path.dirname(os.path.dirname(__file__)))  
_ANALYSIS_DIR = os.path.dirname(_ANALYSIS_DIR)  
CONFIG_DIR = os.path.abspath(os.path.join(_ANALYSIS_DIR, 'config'))


def _load_yaml_config(path: str) -> Dict[str, Any]:
    if yaml is None:
        raise RuntimeError("PyYAML is required to read config files. Please install pyyaml.")
    if not os.path.exists(path):
        raise FileNotFoundError(f"GPU config file not found: {path}")
    with open(path, 'r', encoding='utf-8') as f:
        data = yaml.safe_load(f) or {}
    if not isinstance(data, dict):
        raise ValueError(f"Invalid YAML structure in {path}: expected a mapping at the root.")
    return data


def _get_enum_value(enum_cls, name: str, *, default=None):
    if name is None:
        return default
    if isinstance(name, enum_cls):
        return name
    if not isinstance(name, str):
        raise ValueError(f"Expected string for {enum_cls.__name__}, got {type(name).__name__}")
    
    key = name.strip()
    if hasattr(enum_cls, key):
        return getattr(enum_cls, key)
    
    for attr in dir(enum_cls):
        if attr.upper() == key.upper() and not attr.startswith('_'):
            return getattr(enum_cls, attr)
    
    try:
        return enum_cls[key]  
    except Exception:
        pass
    raise ValueError(f"Unknown {enum_cls.__name__} value: {name}")


def _build_training_model(cfg: Dict[str, Any]) -> TrainingModel:
    builder = TrainingModel().builder()

    
    dataset_size = cfg.get('dataset_size', 'MICRO')
    builder = builder.max_rows(_get_enum_value(DatasetSize, dataset_size))

    
    epoch_focus = cfg.get('epochs', 'SMALL_ANALYSIS')
    builder = builder.epochs(_get_enum_value(EpochFocus, epoch_focus))

    
    batch_size = cfg.get('batch_size', 'BALANCED')
    builder = builder.batch_size(_get_enum_value(BatchSizeEvaluations, batch_size))

    
    seq_len = cfg.get('sequence_length', 10)
    if not isinstance(seq_len, int):
        raise ValueError(f"sequence_length must be an integer, got {type(seq_len).__name__}")
    builder = builder.sequence_length(seq_len)

    
    query_type = cfg.get('query_type', 'HISTORICAL_PRICE')
    builder = builder.query_type(_get_enum_value(QueryType, query_type))

    return builder.build()


def _select_currencies(cfg: Dict[str, Any], gpu_id: int) -> List[str] | None:
    """
    Returns a list of currency codes to train, or None if single-currency mode should be used.
    """
    use_generator = bool(cfg.get('use_currency_generator', False))
    if not use_generator:
        return None

    split_by_gpu = bool(cfg.get('split_currencies_by_gpu', False))
    use_cache = bool(cfg.get('generator_use_cache', False))

    if split_by_gpu:
        gpu_zero, gpu_one = get_split_currency_list()
        return gpu_zero if gpu_id == 0 else gpu_one
    
    return get_all_currency_codes(use_cache)


def train_from_config(gpu_id: int) -> None:
    """
    Loads the config for the given GPU id (0 or 1) and runs the training.

    YAML schema (example):
    ---
    dataset_size: MICRO | SMALL | MEDIUM | LARGE | ... (DatasetSize enum member)
    epochs: SMALL_ANALYSIS | COMPLETE_ANALYSIS | ... (EpochFocus enum member)
    batch_size: BALANCED | DEEP_ANALYSIS | ... (BatchSizeEvaluations enum member)
    sequence_length: 10
    query_type: HISTORICAL_PRICE | ... (QueryType enum member)
    target_currency: BTC
    model_type: LSTM | ... (ModelType enum member)
    use_currency_generator: false
    generator_use_cache: false
    split_currencies_by_gpu: false
    """
    setup_logging()
    setup_tensorflow_env()

    config_filename = f"gpu_{gpu_id}_config.yml"
    config_path = os.path.join(CONFIG_DIR, config_filename)

    cfg = _load_yaml_config(config_path)

    training_model = _build_training_model(cfg)

    model_type = _get_enum_value(ModelType, cfg.get('model_type', 'LSTM'))

    
    currencies = _select_currencies(cfg, gpu_id)
    if currencies is None:
        
        target_currency = cfg.get('target_currency', 'BTC')
        if not isinstance(target_currency, str):
            raise ValueError("target_currency must be a string symbol like 'BTC'")
        session = TrainingSession(
            gpu_id=gpu_id,
            training_model=training_model,
            target_currency=target_currency,
            model_type=model_type,
        )
        session.train()
        return

    for code in currencies:
        session = TrainingSession(
            gpu_id=gpu_id,
            training_model=training_model,
            target_currency=str(code),
            model_type=model_type,
        )
        session.train()
