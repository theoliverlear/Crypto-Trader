from attr import attr
from attrs import define

from apps.news.models.analysis.sentiment.sentiment_model import SentimentModel


@define
class SentimentAnalyzer:
    model: SentimentModel = attr(default=SentimentModel())

    def get_sentiment(self, text: str):
        return self.model.score_text(text)