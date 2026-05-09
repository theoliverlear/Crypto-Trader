package org.cryptotrader.simulator.library.events

import org.cryptotrader.simulator.library.communication.request.PortfolioSimulationRequest
import org.cryptotrader.simulator.library.communication.response.PortfolioSimulationResponse
import org.cryptotrader.simulator.library.services.SimulatorService
import org.cryptotrader.universal.library.events.EventPublisher
import org.cryptotrader.universal.library.events.RequestGatewayController
import org.cryptotrader.universal.library.events.model.EventBinding
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.time.Duration
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ConcurrentHashMap



@Component
class SimulatorRequestGateway @Autowired constructor(
    override val eventPublisher: EventPublisher,
    private val simulatorService: SimulatorService
) : RequestGatewayController<PortfolioSimulationRequest, PortfolioSimulationResponse> () {
    override val pendingReplies: MutableMap<String, CompletableFuture<PortfolioSimulationResponse>> =
        ConcurrentHashMap<String, CompletableFuture<PortfolioSimulationResponse>>()


    override fun execute(
        binding: EventBinding,
        request: PortfolioSimulationRequest
    ): PortfolioSimulationResponse {
        return this.execute(binding, request, Duration.ofSeconds(30))
    }

    override fun execute(
        binding: EventBinding,
        request: PortfolioSimulationRequest,
        timeout: Duration
    ): PortfolioSimulationResponse {
        return this.execute(binding, request, timeout, null)
    }
}