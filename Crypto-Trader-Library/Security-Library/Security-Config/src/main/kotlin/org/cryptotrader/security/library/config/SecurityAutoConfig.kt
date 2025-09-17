package org.cryptotrader.security.library.config

import org.cryptotrader.security.library.event.SecurityEventLogger
import org.cryptotrader.security.library.infrastructure.IpBanFilter
import org.cryptotrader.security.library.repository.BannedIpRepository
import org.cryptotrader.security.library.service.EncryptionService
import org.cryptotrader.security.library.service.InMemoryIpBanService
import org.cryptotrader.security.library.service.IpBanService
import org.cryptotrader.security.library.service.SecurityThreatService
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@AutoConfiguration
@EnableConfigurationProperties(SecurityPropertiesConfig::class)
@EnableJpaRepositories(basePackages = ["org.cryptotrader.security.library.repository"])
@EntityScan(basePackages = ["org.cryptotrader.security.entity"])
open class SecurityAutoConfig {

    @Bean
    @ConditionalOnMissingBean
    open fun ipBanService(properties: SecurityPropertiesConfig): IpBanService {
        val banService = InMemoryIpBanService()
        properties.bans.denylist.forEach { ipOrCidr ->
            banService.ban(ipOrCidr)
        }
        return banService
    }

    @Bean
    @ConditionalOnMissingBean
    open fun encryptionService(properties: SecurityPropertiesConfig): EncryptionService {
        return EncryptionService(properties)
    }

    @Bean
    @ConditionalOnBean(BannedIpRepository::class)
    open fun securityEventLogger(repository: BannedIpRepository): SecurityEventLogger {
        return SecurityEventLogger(repository)
    }

    @Bean
    @ConditionalOnMissingBean
    open fun handleSecurityThreat(
        ipBanService: IpBanService,
        properties: SecurityPropertiesConfig
    ): SecurityThreatService = SecurityThreatService(ipBanService, properties)

    @Bean
    open fun ipBanFilterRegistration(
        ipBanService: IpBanService,
        properties: SecurityPropertiesConfig
    ): FilterRegistrationBean<IpBanFilter> {
        val filter = IpBanFilter(ipBanService, properties.http.blockResponseCode)
        val registration = FilterRegistrationBean(filter)
        registration.order = -100
        registration.setName("ipBanFilter")
        registration.isEnabled = properties.bans.enabled
        return registration
    }
}
