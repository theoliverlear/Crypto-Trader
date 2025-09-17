package org.cryptotrader.security.library.service

interface IpBanService {
    fun isBanned(ip: String): Boolean
    fun ban(ipOrCidr: String)
    fun unban(ipOrCidr: String)
}