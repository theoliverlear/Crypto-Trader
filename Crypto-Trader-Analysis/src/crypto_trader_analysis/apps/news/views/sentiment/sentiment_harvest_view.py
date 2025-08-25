import logging

from rest_framework.response import Response
from rest_framework.views import APIView

from src.crypto_trader_analysis.apps.news.models.fetch_articles import get_by_day, load_to_json
from src.crypto_trader_analysis.apps.news.models.analysis.parse_sentiment import capture_latest_sentiment

class SentimentHarvestView(APIView):
    def post(self, request):
        try:
            payload: dict = request.data
            num_articles: int = payload.get("numArticles")
            days_offset: int = payload.get("daysOffset")
            num_days: int = payload.get("numDays")
            include_forbes = payload.get("includeForbes")
            articles = get_by_day(
                num_articles=num_articles,
                days_offset=days_offset,
                num_days=num_days,
                include_forbes=include_forbes,
            )
            load_to_json(articles)
            capture_latest_sentiment()
            return Response(status=200)
        except Exception as exception:
            import traceback
            stack_trace: str = traceback.format_exc()
            logging.error(stack_trace)
            return Response(status=500, data={"error": str(exception)})
        