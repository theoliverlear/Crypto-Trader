package org.cryptotrader.security.library.service

import inet.ipaddr.IPAddress
import inet.ipaddr.IPAddressString
import java.util.concurrent.ConcurrentHashMap

class InMemoryIpBanService : IpBanService {
    private val _blockedAddresses: MutableMap<String, IPAddressString> =
        ConcurrentHashMap()
    val blockedAddresses: MutableMap<String, IPAddressString>
        get() = this._blockedAddresses

    override fun isBanned(ip: String): Boolean {
        val clientAddress: IPAddress = this.parseAddress(ip) ?: return false
        if (this.blockedAddresses.containsKey(ip)) return true
        for (blocked in this.blockedAddresses.values) {
            val blockedAddress: IPAddress = blocked.address ?: continue
            if (blockedAddress.contains(clientAddress) || blockedAddress == clientAddress) {
                return true
            }
        }
        return false
    }

    override fun ban(ipOrCidr: String) {
        this.blockedAddresses[ipOrCidr] = IPAddressString(ipOrCidr)
    }

    override fun unban(ipOrCidr: String) {
        this.blockedAddresses.remove(ipOrCidr)
    }

    private fun parseAddress(value: String): IPAddress? = IPAddressString(
        value
    ).address
}