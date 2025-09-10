package org.cryptotrader.api.library.events

import org.cryptotrader.api.library.entity.user.User
import java.time.LocalDateTime

// TODO: Expand this to determine whether this was OAuth (ex. Sign up via
//       Google account, Coinbase account, etc..
data class UserRegisteredEvent(
    val user: User, 
    val dateTime: LocalDateTime)
