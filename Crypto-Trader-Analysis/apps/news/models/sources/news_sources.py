import json

from apps.news.models.sources.news_source import NewsSource

def get_news_sources_dict() -> dict:
    with open('sources/news_sources.json') as f:
        data = json.load(f)
    return data

def load_news_sources() -> list[NewsSource]:
    news_sources_dict: dict = get_news_sources_dict()
    news_sources: list[NewsSource] = []
    for key, value in news_sources_dict.items():
        news_sources.append(NewsSource(key, value))
    return news_sources

def get_by_link(url: str) -> NewsSource | None:
    for news_source in NEWS_SOURCES:
        if news_source.url in url:
            return news_source
    return None


NEWS_SOURCES: list[NewsSource] = load_news_sources()
NEWS_SOURCES_LINKS: list[str] = [source.url for source in NEWS_SOURCES]