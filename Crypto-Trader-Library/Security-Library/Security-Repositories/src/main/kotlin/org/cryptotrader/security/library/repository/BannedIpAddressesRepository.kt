package org.cryptotrader.security.library.repository

import org.cryptotrader.security.library.entity.BannedIpAddress
import org.springframework.data.jpa.repository.JpaRepository

interface BannedIpAddressesRepository : JpaRepository<BannedIpAddress, Long>