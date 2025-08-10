from attr import attr
from attrs import define


@define
class Article:
    article_id: int = attr(default=0)
    title: str = attr(default="")
    publish_date: str = attr(default="")
    source: str = attr(default="")
    url: str = attr(default="")
    text: str = attr(default="")
    
    def as_scored_article(self):
        from .scored_article import ScoredArticle
        return ScoredArticle(
            article_id=self.article_id,
            title=self.title,
            publish_date=self.publish_date,
            source=self.source,
            url=self.url,
            text=self.text
        )