package org.cryptotrader.security.library.service

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Service

@Service
class SecurityThreatService(
    private val ipBanService: IpBanService,
    private val blockResponseCode: Int = 404
) {
    fun handle(request: HttpServletRequest, response: HttpServletResponse, next: () -> Unit) {
        val clientIp = request.remoteAddr
        if (this.isClientBlocked(clientIp)) {
            this.blockRequest(response)
            return
        }
        next()
    }

    fun handle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain
    ) {
        this.handle(request, response) {
            chain.doFilter(request, response) 
        }
    }

    private fun isClientBlocked(clientIp: String): Boolean {
        return this.ipBanService.isBanned(clientIp)
    }

    private fun blockRequest(response: HttpServletResponse) {
        response.status = this.blockResponseCode
        response.flushBuffer()
    }
}