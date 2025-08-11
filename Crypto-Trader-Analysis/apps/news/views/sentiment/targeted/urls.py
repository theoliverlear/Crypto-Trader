from django.urls import path

from apps.news.views.sentiment.targeted.date_targeted_sentiment_harvest_view import \
    DateTargetedSentimentHarvestView

urlpatterns = [
    path('by-date', DateTargetedSentimentHarvestView.as_view(), name='targeted-sentiment-by-date')
]