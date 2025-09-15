package org.cryptotrader.api.library.model.currency

enum class PerformanceRating(val rating: String) {
    UP("up"),
    DOWN("down"),
    NEUTRAL("neutral");
    companion object {
        @JvmStatic
        fun fromValues(oldValue: Double, newValue: Double): PerformanceRating =
            fromDelta(newValue - oldValue)

        @JvmStatic
        fun fromDelta(delta: Double): PerformanceRating =
            when {
                delta > 0 -> UP
                delta < 0 -> DOWN
                else -> NEUTRAL
            }
    }
}