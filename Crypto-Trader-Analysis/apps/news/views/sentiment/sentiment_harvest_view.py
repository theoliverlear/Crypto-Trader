from rest_framework.response import Response
from rest_framework.views import APIView

from apps.news.models.fetch_articles import get_by_day, load_to_json
from apps.news.models.analysis.parse_sentiment import capture_latest_sentiment

class SentimentHarvestView(APIView):
    def post(self, request):
        try:
            payload: dict = request.data
            num_articles: int = payload.get("numArticles")
            days_offset: int = payload.get("daysOffset")
            num_days: int = payload.get("numDays")
            print(payload)
            articles = get_by_day(
                num_articles=num_articles,
                days_offset=days_offset,
                num_days=num_days
            )
            load_to_json(articles)
            capture_latest_sentiment()
            return Response(status=200)
        except Exception as e:
            print(f"Error: {e}")
            import traceback
            stack_trace = traceback.format_exc()
            print(stack_trace)
            return Response(status=500, data={"error": str(e)})
        