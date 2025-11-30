@file:JvmName("AuthAliases")
package org.cryptotrader.api.library.communication.request.alias

import org.cryptotrader.api.library.communication.response.AuthResponse
import org.cryptotrader.universal.library.model.http.PayloadStatusResponse

typealias HttpAuthStatus = PayloadStatusResponse<AuthResponse>