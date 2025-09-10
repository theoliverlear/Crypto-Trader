package org.cryptotrader.api.library.events

import org.cryptotrader.api.library.entity.user.User
import java.time.LocalDateTime

data class UserLoginEvent(
    val user: User,
    val dateTime: LocalDateTime)
