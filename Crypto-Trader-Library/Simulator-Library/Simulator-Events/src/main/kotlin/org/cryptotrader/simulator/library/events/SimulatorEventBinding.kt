package org.cryptotrader.simulator.library.events

import org.cryptotrader.universal.library.events.model.EventBinding

enum class SimulatorEventBinding(override val bindingName: String) : EventBinding {
    PORTFOLIO_SIMULATION_REQUEST("portfolioSimulationRequest-out-0"),
    PORTFOLIO_SIMULATION_REPLY("portfolioSimulationReply-out-0"),
    ASSET_SIMULATION_REQUEST("assetSimulationRequest-out-0"),
    ASSET_SIMULATION_REPLY("assetSimulationReply-out-0")
}