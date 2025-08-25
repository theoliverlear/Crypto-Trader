# sentiment.py
import json
import logging
from pathlib import Path

from src.crypto_trader_analysis.apps.news.models.article.article import Article
from src.crypto_trader_analysis.apps.news.models.article.scored_article import ScoredArticle


def find_latest_data_file(base_dir: Path) -> Path | None:
    candidates: list[Path] = list(base_dir.glob("temp_data_*.json"))
    if not candidates:
        return None
    return max(candidates, key=lambda latest_file: latest_file.stat().st_mtime)

def load_articles() -> list[dict]:
    news_dir = get_storage_directory()
    latest_file: Path | None = find_latest_data_file(news_dir)
    if latest_file is None:
        raise FileNotFoundError(f"No temp_data_*.json files found in {news_dir}")
    try:
        with latest_file.open("r", encoding="utf-8") as file:
            json_data: object = json.load(file)
    except json.JSONDecodeError as exception:
        raise ValueError(f"Failed to parse JSON from {latest_file}: {exception}") from exception

    if not isinstance(json_data, list):
        raise ValueError(f"Unexpected JSON structure in {latest_file}: expected a list, got {type(json_data).__name__}")

    all_articles: list[dict] = []
    for day_entry in json_data:
        if isinstance(day_entry, dict) and isinstance(day_entry.get("articles"), list):
            all_articles.extend(day_entry["articles"])
    return all_articles


def get_storage_directory() -> Path:
    storage_dir: str = "data\\temp"
    news_dir: Path = Path(__file__).resolve().parents[4] / storage_dir
    return news_dir


def delete_file(file_path: Path) -> None:
    file_path.unlink(missing_ok=True)
    logging.debug(f"Deleted {file_path.name} (at {file_path.parent.name} folder)")

def capture_latest_sentiment() -> None:
    if get_num_files_in_dir(get_storage_directory()) < 1:
        logging.debug("No articles to score")
        return
    articles: list[dict] = load_articles()
    scored_articles: list[ScoredArticle] = []
    for article in articles:
        article_id: int = article["id"]
        title: str = article["title"]
        publish_date: str = article["publish_date"]
        source: str = article["source"]
        url: str = article["url"]
        text: str = article["text"]
        logging.debug(f"Article {article_id}: {title} ({source})")
        news_article: Article = Article(
            article_id=article_id,
            title=title,
            publish_date=publish_date,
            source=source,
            url=url,
            text=text
        )
        scored_article: ScoredArticle = news_article.as_scored_article()
        is_unique_article: bool = True
        if len(articles) > 1:
            for found_article in scored_articles:
                if scored_article.title == found_article.title:
                    is_unique_article = False
                    break
        if is_unique_article:
            scored_articles.append(scored_article)
            scored_article.score_article(False)
    for scored_article in scored_articles:
        print(scored_article)
        scored_article.send_to_server()
    delete_file(get_storage_directory() / find_latest_data_file(get_storage_directory()))

def get_num_files_in_dir(dir_path: Path) -> int:
    return len(list(dir_path.glob("temp_data_*.json")))

if __name__ == "__main__":
    for i in range(3):
        capture_latest_sentiment()
