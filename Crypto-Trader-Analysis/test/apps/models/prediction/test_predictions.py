import unittest

from apps.models.prediction.predictions import model_exists, get_current_price


class TestPredictions(unittest.TestCase):
    def test_model_exists(self):
        model_found: bool = model_exists('LTC')
        self.assertTrue(model_found, "Model should exist for LTC")

    def test_get_current_price(self):
        current_price: float = get_current_price("BTC")
        print(current_price)
        self.assertTrue(current_price > 0, "Current price should be greater than 0")

if __name__ == '__main__':
    unittest.main()
