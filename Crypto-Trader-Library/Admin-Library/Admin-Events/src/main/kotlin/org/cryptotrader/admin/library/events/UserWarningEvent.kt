package org.cryptotrader.admin.library.events

import org.cryptotrader.admin.library.models.WarnOffense
import org.cryptotrader.api.library.entity.user.User

data class UserWarningEvent(
    val user: User,
    val offense: WarnOffense
)
