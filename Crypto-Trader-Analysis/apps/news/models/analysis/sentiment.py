# sentiment.py
import json
from pathlib import Path

from apps.news.models.article.article import Article
from apps.news.models.article.scored_article import ScoredArticle

# TODO: Add delete function once used.

def find_latest_data_file(base_dir: Path) -> Path | None:
    candidates: list[Path] = list(base_dir.glob("temp_data_*.json"))
    if not candidates:
        return None
    return max(candidates, key=lambda latest_file: latest_file.stat().st_mtime)

def load_articles() -> list[dict]:
    news_dir: Path = Path(__file__).resolve().parents[1]  # apps/news
    latest_file: Path | None = find_latest_data_file(news_dir)

    if latest_file is None:
        raise FileNotFoundError(f"No temp_data_*.json files found in {news_dir}")

    try:
        with latest_file.open("r", encoding="utf-8") as file:
            json_data: object = json.load(file)
    except json.JSONDecodeError as e:
        raise ValueError(f"Failed to parse JSON from {latest_file}: {e}") from e

    if not isinstance(json_data, list):
        raise ValueError(f"Unexpected JSON structure in {latest_file}: expected a list, got {type(json_data).__name__}")

    all_articles: list[dict] = []
    for day_entry in json_data:
        if isinstance(day_entry, dict) and isinstance(day_entry.get("articles"), list):
            all_articles.extend(day_entry["articles"])
    return all_articles

def capture_latest_sentiment():
    articles: list[dict] = load_articles()
    print(f"Loaded {len(articles)} articles")
    scored_articles: list[ScoredArticle] = []
    for article in articles:
        article_id: int = article["id"]
        title: str = article["title"]
        publish_date: str = article["publish_date"]
        source: str = article["source"]
        url: str = article["url"]
        text: str = article["text"]
        print(f"Article {article_id}: {title} ({source})")
        news_article: Article = Article(
            article_id=article_id,
            title=title,
            publish_date=publish_date,
            source=source,
            url=url,
            text=text
        )
        scored_article: ScoredArticle = news_article.as_scored_article()
        scored_article.score_article()
        scored_articles.append(scored_article)
    for scored_article in scored_articles:
        print(scored_article)

if __name__ == "__main__":
    capture_latest_sentiment()
