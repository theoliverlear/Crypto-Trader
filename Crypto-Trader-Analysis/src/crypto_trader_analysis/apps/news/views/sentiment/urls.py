from django.urls import path, include

from src.crypto_trader_analysis.apps.news.views.sentiment.sentiment_harvest_view import \
    SentimentHarvestView

urlpatterns = [
    path('harvest', SentimentHarvestView.as_view(), name='news-sentiment'),
    path('harvest/targeted/', include('src.crypto_trader_analysis.apps.news.views.sentiment.targeted.urls'))
]