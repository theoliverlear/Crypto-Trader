package org.cryptotrader.admin.library.events

import org.cryptotrader.admin.library.models.BanOffense
import org.cryptotrader.api.library.entity.user.User

data class UserBannedEvent(
    val user: User,
    val offense: BanOffense
)
