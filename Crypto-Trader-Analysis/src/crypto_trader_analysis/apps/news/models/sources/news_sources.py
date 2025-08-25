import json
from importlib import resources as _res

from src.crypto_trader_analysis.apps.news.models.sources.news_source import NewsSource

def get_news_sources_dict() -> dict:
    with _res.files(__package__).joinpath('news_sources.json').open('r', encoding='utf-8') as file:
        data = json.load(file)
    return data

def load_news_sources() -> list[NewsSource]:
    news_sources_dict: dict = get_news_sources_dict()
    news_sources: list[NewsSource] = []
    for key, value in news_sources_dict.items():
        news_sources.append(NewsSource(key, value))
    return news_sources

def get_by_link(url: str) -> NewsSource | None:
    url = strip_http(url)
    for news_source in ALL_NEWS_SOURCES:
        source_url = strip_http(news_source.url)
        if source_url in url:
            return news_source
    return None


def strip_http(url: str) -> str:
    if ("https://" in url) or ("http://" in url):
        url = url.replace("https://", "").replace("http://", "")
    return url


ALL_NEWS_SOURCES: list[NewsSource] = load_news_sources()
ALL_NEWS_SOURCES_LINKS: list[str] = [source.url for source in ALL_NEWS_SOURCES]
FILTERED_NEWS_SOURCES: list[NewsSource] = [source for source in ALL_NEWS_SOURCES if source.name != "Forbes"]
FILTERED_NEWS_SOURCES_LINKS: list[str] = [source.url for source in FILTERED_NEWS_SOURCES]