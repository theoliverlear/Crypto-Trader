from abc import ABC, abstractmethod


class BaseModel(ABC):
    @abstractmethod
    def train(self,
              training_data,
              target_values,
              epochs: int = 20,
              batch_size: int = 32):
        pass

    @abstractmethod
    def predict(self,
                training_data,
                scaler):
        pass

    @abstractmethod
    def save_model(self,
                   path: str):
        pass

    @abstractmethod
    def load_model(self,
                   path: str):
        pass