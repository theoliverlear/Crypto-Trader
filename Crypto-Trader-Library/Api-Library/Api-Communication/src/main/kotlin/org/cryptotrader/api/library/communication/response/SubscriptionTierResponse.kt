package org.cryptotrader.api.library.communication.response

import org.cryptotrader.api.library.entity.user.SubscriptionTier

data class SubscriptionTierResponse(
    val subscriptionTier: String
) {
    constructor(subscriptionTier: SubscriptionTier) : this(subscriptionTier.name)
}
