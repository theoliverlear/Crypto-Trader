package org.cryptotrader.api.controller

import org.cryptotrader.simulator.library.communication.request.PortfolioSimulationRequest
import org.cryptotrader.simulator.library.events.SimulatorEventBinding
import org.cryptotrader.simulator.library.events.SimulatorRequestGateway
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api/simulator")
@RestController
class SimulatorController @Autowired constructor(
    private val simulatorRequestGateway: SimulatorRequestGateway
) {
    @PostMapping("/simulate")
    fun simulate(@RequestBody simulationRequest: PortfolioSimulationRequest) {
        this.simulatorRequestGateway.execute(
            SimulatorEventBinding.PORTFOLIO_SIMULATION_REQUEST,
            simulationRequest
        )
    }
}