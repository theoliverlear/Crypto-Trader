package org.cryptotrader.simulator.library.communication.response

data class PortfolioSimulationResponse(
    val cryptoTraderProfit: Double,
    val naturalProfit: Double,
    // TODO: Add natural and service spark points.
    )