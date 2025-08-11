from django.urls import path, include

urlpatterns = [
    path('sentiment/', include('apps.news.views.sentiment.urls'))
]