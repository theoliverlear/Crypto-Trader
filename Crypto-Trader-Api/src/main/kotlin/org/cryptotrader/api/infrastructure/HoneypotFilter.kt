package org.cryptotrader.api.infrastructure

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.cryptotrader.api.service.HoneypotService
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 10)
class HoneypotFilter(
    private val honeypotService: HoneypotService
) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain
    ) {
        if (honeypotService.captureHoneypot(request) != null) {
            response.status = HttpServletResponse.SC_NOT_FOUND
            return
        }
        chain.doFilter(request, response)
    }
}