package org.cryptotrader.api.library.services

import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class AuthContextService {
    fun isAuthenticated(): Boolean {
        val authentication: Authentication? = SecurityContextHolder.getContext().authentication
        return authentication != null && authentication.isAuthenticated
    }

    fun logout() {
        SecurityContextHolder.clearContext()
    }
}