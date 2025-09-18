package org.cryptotrader.security.library.infrastructure

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.cryptotrader.security.library.service.IpBanService
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.web.filter.OncePerRequestFilter

@Order(Ordered.HIGHEST_PRECEDENCE + 10)
class IpBanFilter(
    private val ipBanService: IpBanService,
    private val blockResponseCode: Int
) : OncePerRequestFilter() {

    override fun doFilterInternal(request: HttpServletRequest, 
                                  response: HttpServletResponse,
                                  filterChain: FilterChain) {
        val clientIp = request.remoteAddr
        if (this.isClientBanned(clientIp)) {
            this.blockRequest(response)
            return
        }
        filterChain.doFilter(request, response)
    }

    private fun isClientBanned(clientIp: String): Boolean {
        return this.ipBanService.isBanned(clientIp)
    }

    private fun blockRequest(response: HttpServletResponse) {
        response.status = this.blockResponseCode
        response.flushBuffer()
    }
}