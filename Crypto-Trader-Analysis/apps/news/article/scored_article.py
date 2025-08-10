from attr import attr
from attrs import define

from apps.news.analysis.sentiment.sentiment_analyzer import SentimentAnalyzer
from apps.news.analysis.sentiment.sentiment_result import SentimentResult
from apps.news.article.article import Article

@define
class ScoredArticle(Article):
    sentiment_result: SentimentResult = attr(default=None)
    
    def score_article(self) -> None:
        sentiment_analyzer: SentimentAnalyzer = SentimentAnalyzer()
        self.sentiment_result = sentiment_analyzer.get_sentiment(self.text)
        
    def to_json(self):
        return {
            "articleId": self.article_id,
            "title": self.title,
            "publishDate": self.publish_date,
            "source": self.source,
            "url": self.url,
            "positiveScore": self.sentiment_result.positive_score,
            "neutralScore": self.sentiment_result.neutral_score,
            "negativeScore": self.sentiment_result.negative_score,
            "compositeScore": self.sentiment_result.composite_score,
            "cryptoRelevance": self.sentiment_result.crypto_relevance,
        }