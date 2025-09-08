package org.cryptotrader.admin.model

data class ChartDataPoint<X, Y>(
    val x: X,
    val y: Y
)