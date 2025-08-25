from attr import attr, asdict
from attrs import define

from src.crypto_trader_analysis.apps.news.models.analysis.sentiment.sentiment_label import SentimentLabel


@define
class SentimentResult:
    label: SentimentLabel = attr(default=None)
    positive_score: float = attr(default=0.0)
    neutral_score: float = attr(default=0.0)
    negative_score: float = attr(default=0.0)
    composite_score: float = attr(default=0.0)
    crypto_relevance: float = attr(default=0.0)
    content_hash_hex: str = attr(default=None)

    def to_dict(self) -> dict:
        return asdict(self)
    
    def __str__(self) -> str:
        return f"""
        Positive: {self.positive_score}
        Neutral: {self.neutral_score}
        Negative: {self.negative_score}
        Composite: {self.composite_score}
        Crypto Relevance: {self.crypto_relevance}
        Content Hash: {self.content_hash_hex}
        """