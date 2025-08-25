import hashlib
from typing import Iterable

from attr import attr
from attrs import define
from transformers import AutoTokenizer, AutoModelForSequenceClassification, \
    pipeline

import re as regex

from src.crypto_trader_analysis.apps.news.models.analysis.crypto_regex import CRYPTO_PATTERN
from src.crypto_trader_analysis.apps.news.models.analysis.sentiment.sentiment_label import SentimentLabel
from src.crypto_trader_analysis.apps.news.models.analysis.sentiment.sentiment_result import SentimentResult


@define
class SentimentModel:
    MODEL_NAME: str = "ProsusAI/finbert"
    MAX_TOKENS: int = 512
    CHAR_CHUNK_SIZE: int = 2000
    model_version: str = attr(default="finbert_v1")
    tokenizer = attr(default=AutoTokenizer.from_pretrained(MODEL_NAME))
    sequence_classifier = attr(default=AutoModelForSequenceClassification.from_pretrained(MODEL_NAME))
    classification_pipeline = attr(default=None)
    
    def __attrs_post_init__(self):
        self.classification_pipeline = pipeline(
            "text-classification",
            model=self.sequence_classifier,
            tokenizer=self.tokenizer,
            top_k=None,
            truncation=True
        )
        

    def token_count(self, text: str) -> int:
        tokens = self.tokenizer(text,
                                truncation=True, 
                                max_length=SentimentModel.MAX_TOKENS,
                                add_special_tokens=False)
        return len(tokens["input_ids"]) or 1

    def split_sentences(self, text: str) -> list[str]:
        try:
            import syntok.segmenter as segmenter
            tokens = [" ".join(token.value for token in sentence_tokens).strip()
                      for tokenized_sentences in segmenter.analyze(text) for
                      sentence_tokens in tokenized_sentences if sentence_tokens]
            return tokens
        except Exception:
            stripped_tokens = [sentence_tokens.strip() for sentence_tokens in
                               regex.split(r'(?<=[\.\?\!])\s+', text) if
                               sentence_tokens.strip()]
            return stripped_tokens
    
    def filter_crypto_sentences(self, sentences: Iterable[str]) -> tuple[list[str], float]:
        sentences: list[str] = list(sentences)
        hits: list[str]  = [sentence for sentence in sentences if CRYPTO_PATTERN.search(sentence)]
        relevance = (len(hits) / len(sentences)) if sentences else 0.0
        return (hits if hits else sentences), relevance
    
    def chunk_text(self, sentences: list[str]) -> list[str]:
        chunks, buffer = [], ""
        for sentence in sentences:
            if buffer and len(buffer) + 1 + len(sentence) > SentimentModel.CHAR_CHUNK_SIZE:
                chunks.append(buffer)
                buffer = sentence
            else:
                buffer = sentence if not buffer else f"{buffer} {sentence}"
        if buffer:
            chunks.append(buffer)
        return chunks

    def classify_chunk(self, text: str) -> dict:
        scores = self.classification_pipeline(text,
                                              max_length=SentimentModel.MAX_TOKENS,
                                              truncation=True)[0]
        out = {score["label"].lower(): float(score["score"]) for score in scores}
        return {
            "positive": out.get("positive", 0.0),
            "neutral": out.get("neutral", 0.0),
            "negative": out.get("negative", 0.0),
        }

    def aggregate_weighted(self, chunks: Iterable[str]) -> tuple[float, float, float]:
        total_words: int = 0
        positive_sum: float = 0.0
        neutral_sum: float = 0.0
        negative_sum: float = 0.0
        for chunk in chunks:
            tokens = self.token_count(chunk)
            chunk_score = self.classify_chunk(chunk)
            positive_sum += chunk_score["positive"] * tokens
            neutral_sum += chunk_score["neutral"] * tokens
            negative_sum += chunk_score["negative"] * tokens
            total_words += tokens
        if total_words == 0:
            return 0.0, 1.0, 0.0
        positive_score = positive_sum / total_words
        neutral_score = neutral_sum / total_words
        negative_score = negative_sum / total_words
        return positive_score, neutral_score, negative_score

    def choose_label(self, positive: float, neutral: float, negative: float) -> SentimentLabel:
        top = max((positive, SentimentLabel.POSITIVE),
                  (neutral, SentimentLabel.NEUTRAL),
                  (negative, SentimentLabel.NEGATIVE), key=lambda x: x[0])[1]
        return top

    def score_text(self, text: str) -> SentimentResult:
        if not text or not text.strip():
            return SentimentResult(
                label=SentimentLabel.NEUTRAL,
                positive_score=0.0, neutral_score=1.0, negative_score=0.0,
                composite_score=0.0, crypto_relevance=0.0,
                content_hash_hex=hashlib.sha256(b"").hexdigest(),
            )

        sentences = self.split_sentences(text)
        focus, relevance = self.filter_crypto_sentences(sentences)
        chunks = self.chunk_text(focus)
        pos, neu, neg = self.aggregate_weighted(chunks)
        composite = pos - neg
        label = self.choose_label(pos, neu, neg)

        return SentimentResult(
            label=label,
            positive_score=pos,
            neutral_score=neu,
            negative_score=neg,
            composite_score=composite,
            crypto_relevance=relevance,
            content_hash_hex=hashlib.sha256(text.encode("utf-8")).hexdigest(),
        )