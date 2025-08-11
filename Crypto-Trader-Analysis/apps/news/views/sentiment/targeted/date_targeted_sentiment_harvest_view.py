from datetime import date

from rest_framework.views import APIView
from rest_framework.response import Response
from apps.news.models.analysis.parse_sentiment import capture_latest_sentiment
from apps.news.models.fetch_articles import get_by_target, load_to_json


class DateTargetedSentimentHarvestView(APIView):
    def post(self, request):
        try:
            payload: dict = request.data
            num_articles: int = payload.get("numArticles")
            start_date: date = date.fromisoformat(payload.get("startDate"))
            end_date: date = date.fromisoformat(payload.get("endDate"))
            print(payload)
            articles = get_by_target(num_articles=num_articles,
                                     start_date=start_date,
                                     end_date=end_date)
            load_to_json(articles)
            capture_latest_sentiment()
            return Response(status=200)
        except Exception as e:
            print(f"Error: {e}")
            import traceback
            stack_trace = traceback.format_exc()
            print(stack_trace)
            return Response(status=500, data={"error": str(e)})

            
