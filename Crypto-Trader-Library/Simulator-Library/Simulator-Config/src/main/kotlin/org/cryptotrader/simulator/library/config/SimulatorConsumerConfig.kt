package org.cryptotrader.simulator.library.config

import org.cryptotrader.simulator.library.communication.request.PortfolioSimulationRequest
import org.cryptotrader.simulator.library.services.SimulatorService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.messaging.Message
import java.util.function.Consumer

@Configuration
open class SimulatorConsumerConfig @Autowired constructor(
    private val simulatorService: SimulatorService
) {
    @Bean(name = ["simulatorConsumer"])
    open fun simulatorConsumer(): Consumer<Message<PortfolioSimulationRequest>> {
        return Consumer { message ->
            val request = message.payload
            // TODO: Fill this in.
        }
    }
}