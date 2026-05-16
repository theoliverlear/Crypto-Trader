package org.cryptotrader.simulator.library.communication.request

import java.time.LocalDateTime

data class PortfolioSimulationRequest(
    val assetSimulationRequests: List<AssetSimulationRequest>,
    val startDate: LocalDateTime,
    val endDate: LocalDateTime,
)
