package org.cryptotrader.security.library.repository

import org.cryptotrader.security.library.entity.BannedIp
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface BannedIpRepository : JpaRepository<BannedIp, Long>