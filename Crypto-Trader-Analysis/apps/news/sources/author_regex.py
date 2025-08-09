# TODO: Likely going to be deprecated. Forbes is polled to much using this
#       method.

import json
import re as regex
from re import Pattern
from typing import Optional, Iterable, Any

import requests
import tldextract
from bs4 import BeautifulSoup

COMMON_ROLES: set[str] = {
    "staff", "staff writer", "editorial staff", "news desk",
    "opinion staff", "contributors", "contributor", "guest author",
    "agency staff"
}

ALL_ROLES: set[str] = {
    "staff", "staff writer", "editorial staff", "news desk",
    "opinion staff", "contributors", "contributor", "guest author",
    "agency staff", "senior contributor", "contributing writer",
    "columnist", "opinion columnist", "op-ed"
}

ORG_HINTS: set[str] = {
    "forbes", "bloomberg", "reuters", "associated press", "ap news",
    "cnn", "cnbc", "fox", "abc", "nbc", "cbs",
    "united nations", "un ", "eu ", "nato", "government", "ministry",
    "university", "college", "institute", "center", "centre",
    "foundation", "committee", "council", "authority", "press",
    "international", "corp", "inc", "llc", "ltd", "gmbh", "plc",
    "amnesty internation", "amnesty international", "human rights watch"
}

JUNK_TOKENS: set[str] = {"forbes", "newsletter", "update", "analysis", "transcript"}

TITLE_PREFIX_REGEX: Pattern[str] = regex.compile(r"^(dr\.|mr\.|mrs\.|ms\.|prof\.)\s+", regex.I)
NAME_REGEX: Pattern[str] = regex.compile(r"^[A-Z][A-Za-z’'.-]+(?:\s+[A-Z][A-Za-z’'.-]+){1,3}$")
BYLINE_REGEX: Pattern[str] = regex.compile(r"^\s*(?:by|by:)\s+([A-Z][\w’' .-]{2,80})", regex.I | regex.M)

HEADERS: dict[str, str] = {"User-Agent": "Mozilla/5.0 (compatible; NewsFetcher/1.0)"}
USE_HTML_SEARCH = True
HTML_TIMEOUT = 7

def _strip_titles(name: str) -> str:
    return TITLE_PREFIX_REGEX.sub("", name).strip()

def _strip_role_tails(name: str) -> str:
    name = regex.sub(r",\s*(Forbes|Bloomberg|Reuters)\b.*$", "", name, flags=regex.I).strip()
    name = regex.sub(r",\s*(Senior|Chief|Managing|Contributing|Staff|Opinion|Guest|Deputy)\s+\w+.*$", "", name, flags=regex.I).strip()
    return name

def _looks_like_org(name_or_org: str) -> bool:
    name_or_org_lower: str = name_or_org.lower()
    if any(hints in name_or_org_lower for hints in ORG_HINTS):
        return True
    if any(chars.isdigit() for chars in name_or_org):
        return True
    if len(name_or_org.split()) == 1 and name_or_org.isupper():
        return True
    return False

def _is_role_only(name: str) -> bool:
    return name.lower() in ALL_ROLES or "contributor" in name.lower() or "staff" in name.lower() or "editor" in name.lower()

def _is_person_name(possible_name: str) -> bool:
    possible_name = _strip_titles(possible_name)
    possible_name = _strip_role_tails(possible_name)
    if not possible_name:
        return False
    if _is_role_only(possible_name) or _looks_like_org(possible_name):
        return False
    return bool(NAME_REGEX.match(possible_name))

def _split_candidates(candidates: str) -> list[str]:
    split_candidates: list[str] = regex.split(r"\s+(?:and|&)\s+|,\s*", candidates)
    split_candidates = [candidate.strip() for candidate in split_candidates if candidate and candidate.strip()]
    return split_candidates

def byline_from_text(text: str) -> Optional[str]:
    if not text:
        return None
    beginning = text[:1000]
    possible_byline = BYLINE_REGEX.search(beginning)
    if not possible_byline:
        possible_byline = regex.search(r"By\s*([A-Z][\w’' .-]{2,80})", beginning)
        if not possible_byline:
            return None
    byline = possible_byline.group(1)
    byline = _strip_role_tails(byline)
    byline = _strip_titles(byline)
    for candidate in _split_candidates(byline):
        candidate = _strip_titles(_strip_role_tails(candidate))
        if _is_person_name(candidate):
            return candidate
    return None

def normalize_author_field(author: Optional[str]) -> Optional[str]:
    if not author or not isinstance(author, str):
        return None
    author = regex.sub(r'^\s*by\s+', '', author.strip(), flags=regex.I)
    author = _strip_role_tails(author)
    names = []
    for candidates in _split_candidates(author):
        candidates = _strip_titles(_strip_role_tails(candidates))
        if _is_person_name(candidates):
            names.append(candidates)
    if names:
        return names[0]
    return None

def from_authors_list(authors_list: Optional[Iterable[str]]) -> Optional[str]:
    if not authors_list:
        return None
    unique_names: set[str] = set()
    names: list[str] = []
    for author in authors_list:
        if not isinstance(author, str):
            continue
        author = _strip_titles(_strip_role_tails(author.strip()))
        if not author or _is_role_only(author) or _looks_like_org(author):
            continue
        if _is_person_name(author):
            author_lowered: str = author.lower()
            if author_lowered not in unique_names:
                unique_names.add(author_lowered)
                names.append(author)
    return ", ".join(names) if names else None

def choose_author_from_news_item(news_item: dict) -> Optional[str]:
    byline = byline_from_text(news_item.get("text") or "")
    if byline:
        return byline
    author = normalize_author_field(news_item.get("author"))
    if author:
        return author
    return from_authors_list(news_item.get("authors"))

def extract_author_from_html(html: str) -> Optional[str]:
    soup_parser = BeautifulSoup(html, "html.parser")
    candidates = []
    for tag in soup_parser.find_all("script", {"type": "application/ld+json"}):
        try:
            data = json.loads(tag.string or "")
        except Exception:
            continue
        objs = data if isinstance(data, list) else [data]
        for obj in objs:
            if not isinstance(obj, dict):
                continue
            if obj.get("@type") in {"NewsArticle", "Article"}:
                author = obj.get("author")
                if isinstance(author, dict):
                    extracted_name = author.get("name", "")
                    if _is_person_name(extracted_name): candidates.append(extracted_name)
                elif isinstance(author, list):
                    for author in author:
                        extracted_name = author.get("name") if isinstance(author, dict) else str(author)
                        if _is_person_name(extracted_name): candidates.append(extracted_name)
                elif isinstance(author, str) and _is_person_name(author):
                    candidates.append(author)
    for author_properties in [("property", "article:author"), ("name", "author")]:
        key, val = author_properties
        for tag in soup_parser.find_all("meta", {key: val}):
            extracted_name = (tag.get("content") or "").strip()
            if _is_person_name(extracted_name): candidates.append(extracted_name)
    seen_names, names = set(), []
    for candidate in candidates:
        candidate_lowered: str = candidate.lower()
        if candidate_lowered not in seen_names:
            seen_names.add(candidate_lowered)
            names.append(candidate)
    return names[0] if names else None

def source_domain(url: str) -> str:
    extracted_url = tldextract.extract(url)
    return f"{extracted_url.domain}.{extracted_url.suffix}" if extracted_url.suffix else extracted_url.domain

def _is_human_name(possible_name: str) -> bool:
    possible_name = possible_name.strip()
    if not possible_name or len(possible_name) > 80:
        return False
    possible_name_lowered: str = possible_name.lower()
    if possible_name_lowered in COMMON_ROLES:
        return False
    if any(token in possible_name_lowered for token in JUNK_TOKENS):
        return False
    possible_name = regex.sub(r",\s*(senior|chief|managing|contributing|staff)\s+\w+.*$", "", possible_name, flags=regex.I).strip()
    return bool(NAME_REGEX.match(possible_name))

def _clean_tail_roles(author_title: str) -> str:
    author_title = regex.sub(r",\s*(Forbes|Bloomberg|Reuters)\b.*$", "", author_title).strip()
    author_title = regex.sub(r",\s*(Forbes\s+Staff|Staff\s+Writer.*)$", "", author_title, flags=regex.I).strip()
    return author_title

def fetch_html(url: str) -> Optional[str]:
    try:
        response = requests.get(url, headers=HEADERS, timeout=HTML_TIMEOUT)
        if 200 <= response.status_code < 400 and response.text:
            return response.text
    except Exception:
        return None
    return None

def get_author(news_item, url):
    author = choose_author_from_news_item(news_item)
    if USE_HTML_SEARCH and (
            not author or "staff" in (author or "").lower()):
        html = fetch_html(url)
        enriched = extract_author_from_html(html) if html else None
        if enriched:
            author = enriched
    return author