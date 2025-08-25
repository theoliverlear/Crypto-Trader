from enum import Enum


class SequenceLengthSentiment(Enum):
    MICRO_TRENDS = 10
    MINI_TRENDS = 20
    SIGNAL_TRENDS = 30
    CYCLIC_EVENTS = 50
    LONG_TRENDS = 75
    SMALL_SENTIMENT = 125
    STANDARD_SENTIMENT = 150
    LARGE_SENTIMENT = 200
    SEASONALITY = 300

    @staticmethod
    def get_by_time(days: int = 0,
                    hours: int = 0,
                    minutes: int = 0,
                    seconds: int = 0):
        sequences_in_days: int = days * 17280
        sequences_in_hours: int = hours * 720
        sequences_in_minutes: int = minutes * 12
        sequences_in_seconds: int = seconds // 5
        total_sequences: int = (sequences_in_days + sequences_in_hours +
                                sequences_in_minutes + sequences_in_seconds)
        return total_sequences