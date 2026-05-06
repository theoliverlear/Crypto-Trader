package org.cryptotrader.simulator.library.communication.request

data class PortfolioSimulationRequest(
    val assetSimulationRequests: List<AssetSimulationRequest>
)
