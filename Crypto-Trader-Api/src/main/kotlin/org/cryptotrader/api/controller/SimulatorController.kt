package org.cryptotrader.api.controller

import org.cryptotrader.simulator.library.communication.request.PortfolioSimulationRequest
import org.cryptotrader.simulator.library.communication.response.PortfolioSimulationResponse
import org.cryptotrader.simulator.library.events.SimulatorEventBinding
import org.cryptotrader.simulator.library.events.SimulatorRequestGateway
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api/simulator")
@RestController
class SimulatorController @Autowired constructor(
    private val simulatorRequestGateway: SimulatorRequestGateway
) {

    companion object {
        private val log: Logger = LoggerFactory.getLogger(SimulatorController::class.java)
    }

    @PostMapping("/simulate")
    fun simulate(@RequestBody simulationRequest: PortfolioSimulationRequest): ResponseEntity<PortfolioSimulationResponse> {
        log.info("Simulating portfolio: {}", simulationRequest)
        try {
            val result: PortfolioSimulationResponse = this.simulatorRequestGateway.execute(
                SimulatorEventBinding.PORTFOLIO_SIMULATION_REQUEST,
                simulationRequest
            )
            log.info("Simulation result: {}", result)
            return ResponseEntity.ok(result)
        } catch (exception: Exception) {
            log.error("Error simulating portfolio: {}", exception.toString())
            return ResponseEntity.badRequest().build()
        }
    }
}