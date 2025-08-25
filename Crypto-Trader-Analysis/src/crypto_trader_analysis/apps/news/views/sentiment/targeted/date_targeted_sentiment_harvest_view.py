import logging
from datetime import date

from rest_framework.views import APIView
from rest_framework.response import Response
from src.crypto_trader_analysis.apps.news.models.analysis.parse_sentiment import capture_latest_sentiment
from src.crypto_trader_analysis.apps.news.models.fetch_articles import get_by_target, load_to_json


class DateTargetedSentimentHarvestView(APIView):
    def post(self, request):
        try:
            payload: dict = request.data
            num_articles: int = payload.get("numArticles")
            start_date: date = date.fromisoformat(payload.get("startDate"))
            end_date: date = date.fromisoformat(payload.get("endDate"))
            include_forbes = payload.get("includeForbes")
            articles = get_by_target(num_articles=num_articles,
                                     start_date=start_date,
                                     end_date=end_date,
                                     include_forbes=include_forbes)
            load_to_json(articles)
            capture_latest_sentiment()
            return Response(status=200)
        except Exception as exception:
            import traceback
            stack_trace: str = traceback.format_exc()
            logging.error(stack_trace)
            return Response(status=500, data={"error": str(exception)})

            
