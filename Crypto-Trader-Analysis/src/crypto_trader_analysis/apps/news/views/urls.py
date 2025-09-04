from django.urls import path, include

urlpatterns = [
    path('sentiment/', include('src.crypto_trader_analysis.apps.news.views.sentiment.urls'))
]