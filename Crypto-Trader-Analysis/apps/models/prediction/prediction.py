# prediction.py
from datetime import datetime

from attr import attr
from attrs import define


@define
class Prediction:
    prediction_id: int = attr(default=0)
    currency_code: str = attr(default='BTC')
    predicted_price: float = attr(default=0.0)
    actual_price: float = attr(default=0.0)
    price_difference: float = attr(default=0.0)
    percent_difference: float = attr(default=0.0)
    model_type: str = attr(default="lstm")
    num_rows: int = attr(default=10)
    last_updated: datetime = attr(default=datetime.now())

    def to_json(self) -> dict:
        return {
            "currencyCode": self.currency_code,
            "predictedPrice": float(self.predicted_price),
            "actualPrice": float(self.actual_price),
            "priceDifference": float(self.price_difference),
            "percentDifference": float(self.percent_difference),
            "lastUpdated": self.last_updated.utcnow().strftime("%Y-%m-%dT%H:%M:%S"),
            "numRows": int(self.num_rows),
            "modelType": self.model_type
        }

