package org.cryptotrader.api.library.entity.user

enum class SubscriptionTier(val label: String, val level: Int, val intervalMs: Long) {
    FREE("Free", 1, 10000L),
    PRO("Pro", 2, 5000L),
    ELITE("Elite", 3, 1000L);

    companion object {
        fun fromLabel(label: String): SubscriptionTier? {
            return entries.find { it.label == label }
        }
    }
}
