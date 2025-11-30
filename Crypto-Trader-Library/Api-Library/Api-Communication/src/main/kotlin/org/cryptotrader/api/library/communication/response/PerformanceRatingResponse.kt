package org.cryptotrader.api.library.communication.response

import org.cryptotrader.data.library.model.currency.PerformanceRating

data class PerformanceRatingResponse(val rating: String, val changePercent: String) {
    constructor(performanceRating: PerformanceRating, changePercent: String) : this(performanceRating.rating, changePercent)
}