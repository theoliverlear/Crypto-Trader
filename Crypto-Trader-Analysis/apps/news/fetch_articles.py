import json
import re
import time
from datetime import datetime, timezone, timedelta
import os
from typing import Any

import worldnewsapi
from worldnewsapi import SearchNews200Response, ApiResponse

from apps.news.sources.author_regex import get_author
from apps.news.sources.news_sources import NEWS_SOURCES_LINKS, get_by_link

api_key: str = os.getenv("WORLDNEWS_API_KEY")
api_config = worldnewsapi.Configuration(api_key={'apiKey': api_key})
api_instance = worldnewsapi.NewsApi(worldnewsapi.ApiClient(api_config))
# TODO: Optimize and maximize the number of queries. Max 100 chars.
QUERIES = [
    "(crypto OR cryptocurrency OR bitcoin OR btc OR eth OR coinbase OR solana OR stablecoin OR altcoin)"
    # "(crypto OR cryptocurrency OR bitcoin OR btc OR coinbase)",
    # "(ethereum OR eth OR blockchain OR web3 OR defi OR stablecoin)",
    # "(xrp OR solana OR cardano OR bnb OR dogecoin OR litecoin or crypto mining)"
]
SOURCES = ','.join(NEWS_SOURCES_LINKS)
SLEEP_DURATION = 0.6
PAGE_SIZE = 50


def day_bounds_strings(day_offset: int) -> tuple[str, str, str]:
    now_utc: datetime = datetime.now(timezone.utc)
    target: datetime = now_utc - timedelta(days=day_offset)
    day_label: str = target.date().isoformat()
    earliest_date: str = f"{day_label} 00:00:00"
    latest_date: str = f"{day_label} 23:59:59"
    return day_label, earliest_date, latest_date

def get_points_remaining(headers: Any) -> float:
    headers_dict: dict = dict(headers)
    return float(headers_dict.get("X-API-Quota-Left", 0))
    

def fetch_articles(earliest_date: str, latest_date: str, num_articles: int = 50):
    last_points: float = 0.0
    total_points_used: float = 0.0
    collected: dict[str, dict] = {}
    for query in QUERIES:
        offset: int = 0
        while len(collected) < num_articles:
            api_response: ApiResponse[SearchNews200Response] = api_instance.search_news_with_http_info(
                language="en",
                source_country="us",
                news_sources=SOURCES,
                number=PAGE_SIZE,
                categories="business,technology,politics,science",
                sort="publish-time",
                sort_direction="desc",
                text=query,
                earliest_publish_date=earliest_date,
                latest_publish_date=latest_date,
                offset=offset,
            )
            remaining_points: float = get_points_remaining(api_response.headers)
            if last_points != 0:
                points_used: float = last_points - remaining_points
                last_points = remaining_points
                total_points_used += points_used
                print(f"Points used: {points_used:.4f}")
            else:
                last_points = remaining_points
            data: dict = api_response.data.to_dict()
            news_items: list = data.get("news", []) or []
            if not news_items:
                break
            for news_item in news_items:
                print(news_item)
                url = news_item.get("url")
                if not url or url in collected:
                    continue
                author: str = news_item.get("author")
                # author: str = get_author(news_item, url)
                collected[url] = {
                    "id": news_item.get("id"),
                    "title": news_item.get("title"),
                    "url": url,
                    "publish_date": news_item.get("publish_date"),
                    "author": author,
                    "summary": news_item.get("summary"),
                    "language": news_item.get("language"),
                    "source_country": news_item.get("source_country"),
                    "sentiment_api": news_item.get("sentiment"),
                    "text": news_item.get("text"),
                    "keywords": query,
                    "source": get_by_link(url).name if get_by_link(url) else "Unknown",
                }
                if len(collected) >= num_articles:
                    break
            returned: int = len(news_items)
            if returned < PAGE_SIZE:
                break
            offset += returned
            rate_limit_sleep()
        if len(collected) >= num_articles:
            break
        rate_limit_sleep()
    
    points_per_article: float = total_points_used / len(collected)
    print(f"""Used {total_points_used:.4f} points
    Total articles: {len(collected)}
    Points per article: {points_per_article:.4f}
    Points remaining: {last_points:.4f}""")
    return list(collected.values())

# TODO: Use ID system for article fetching. Then once parsed, delete by file
#       matching the ID.
def load_to_json(days_articles: list) -> None:
    timestamp = datetime.now(timezone.utc).strftime("%Y_%m_%d__%H_%M_%S")
    out_path = f"temp_data_{timestamp}.json"
    with open(out_path, "w", encoding="utf-8") as file:
        json.dump(days_articles, file, ensure_ascii=False, indent=2)
    print(f"\nSaved {sum(len(day['articles']) for day in all_days)} articles across {len(all_days)} days → {out_path}")

def rate_limit_sleep():
    time.sleep(SLEEP_DURATION)


def get_by_day(num_days: int, days_offset: int, num_articles: int) -> list[dict]:
    all_days = []
    for day_offset in range(num_days):
        day_label, earliest_str, latest_str = day_bounds_strings(day_offset + days_offset)
        print(
            f"\n=== Collecting for {day_label} (UTC {earliest_str}..{latest_str}) ===")
        items = fetch_articles(earliest_str, latest_str, num_articles=num_articles)
        print(f"Collected {len(items)} articles for {day_label}")
        all_days.append({
            "day": day_label,
            "earliest_publish_date": earliest_str,
            "latest_publish_date": latest_str,
            "articles": items,
        })
    return all_days

# TODO: Make this OOP. Function chaining doesn't do enough to define a type
#       flow.
if __name__ == "__main__":
    all_days = get_by_day(2, 350, 5)
    load_to_json(all_days)