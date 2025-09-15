package org.cryptotrader.api.library.communication.response

import org.cryptotrader.api.library.model.currency.PerformanceRating

data class PerformanceRatingResponse(val rating: String) {
    constructor(performanceRating: PerformanceRating) : this(performanceRating.rating)
}