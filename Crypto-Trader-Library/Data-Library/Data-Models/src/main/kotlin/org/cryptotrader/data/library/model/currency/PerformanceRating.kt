package org.cryptotrader.data.library.model.currency

/**
 * Simple tri-state indicator of price movement for a currency/instrument.
 *
 * Values:
 * - UP:    new value is greater than old value
 * - DOWN:  new value is less than old value
 * - NEUTRAL: values are equal (or delta considered negligible by caller)
 *
 * Utility methods help derive the rating from two values or from a delta.
 */
enum class PerformanceRating(val rating: String) {
    UP("up"),
    DOWN("down"),
    NEUTRAL("neutral");

    companion object {
        /**
         * Determine rating by comparing two values.
         * @param oldValue previous value
         * @param newValue current value
         */
        @JvmStatic
        fun fromValues(oldValue: Double, newValue: Double): PerformanceRating =
            fromDelta(newValue - oldValue)

        /**
         * Determine rating from a signed delta (new - old).
         * Positive -> UP, Negative -> DOWN, Zero -> NEUTRAL.
         */
        @JvmStatic
        fun fromDelta(delta: Double): PerformanceRating =
            when {
                delta > 0 -> UP
                delta < 0 -> DOWN
                else -> NEUTRAL
            }
    }
}