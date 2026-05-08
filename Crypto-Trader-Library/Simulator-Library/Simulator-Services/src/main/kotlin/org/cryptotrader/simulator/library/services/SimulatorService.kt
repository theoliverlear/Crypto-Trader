package org.cryptotrader.simulator.library.services

import org.cryptotrader.api.library.entity.portfolio.Portfolio
import org.cryptotrader.api.library.entity.portfolio.PortfolioAsset
import org.cryptotrader.api.library.entity.user.SubscriptionTier
import org.cryptotrader.data.library.entity.currency.Currency
import org.cryptotrader.data.library.entity.currency.CurrencyHistory
import org.cryptotrader.data.library.repository.CurrencyHistoryRepository
import org.cryptotrader.simulator.library.scripts.getSimulatedAsset
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.sql.Timestamp
import java.time.LocalDateTime

@Service
class SimulatorService @Autowired constructor(
    private val currencyHistoryRepository: CurrencyHistoryRepository
) {

    fun getSimulatedPortfolioAsset(portfolioAsset: PortfolioAsset, startDate: LocalDateTime, endDate: LocalDateTime): PortfolioAsset {
        return getSimulatedAsset(portfolioAsset,
            this.getCurrencyHistoryByPortfolio(
                portfolioAsset.portfolio,
                portfolioAsset.currency,
                startDate,
                endDate
            ))
    }

    fun getCurrencyHistoryByPortfolio(
        portfolio: Portfolio,
        currency: Currency,
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): MutableList<CurrencyHistory> {
        val subscriptionTier: SubscriptionTier = portfolio.user.subscriptionTier
        val currencyCode: String = currency.currencyCode
        val intervalMs: Long = subscriptionTier.intervalMs

        val results = this.currencyHistoryRepository.findHistoryByInterval(
            currencyCode, startDate, endDate, intervalMs
        )

        val resultSet: MutableList<CurrencyHistory> = mutableListOf()

        results.forEach { row ->
            val timestamp: LocalDateTime? = if (row[0] is Timestamp) {
                (row[0] as Timestamp).toLocalDateTime()
            } else {
                row[0] as LocalDateTime?
            }
            val value: Double = (row[1] as Number).toDouble()
            if (timestamp != null) {
                val history = CurrencyHistory()
                history.lastUpdated = timestamp
                history.value = value
                resultSet.add(history)
            }
        }

        return resultSet
    }
}
