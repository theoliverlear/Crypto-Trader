package org.cryptotrader.simulator.library.communication.request

import java.time.LocalDate

data class AssetSimulationRequest(
    val currencyCode: String,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val numShares: Int,
    val numDollars: Int,
    )
