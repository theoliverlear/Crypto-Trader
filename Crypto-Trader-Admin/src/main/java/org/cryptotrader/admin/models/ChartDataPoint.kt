@file:JvmName("ChartDataPoint")
package org.cryptotrader.admin.models

data class ChartDataPoint<X, Y>(
    val x: X,
    val y: Y
)