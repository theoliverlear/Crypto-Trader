package org.cryptotrader.security.library.config

import org.cryptotrader.security.library.entity.key.EncryptedKey
import org.cryptotrader.security.library.model.key.KeyEncrypter
import org.cryptotrader.security.library.infrastructure.IpBanFilter
import org.cryptotrader.security.library.service.EncryptionService
import org.cryptotrader.security.library.service.InMemoryIpBanService
import org.cryptotrader.security.library.service.model.IpBanManager
import org.cryptotrader.security.library.service.IpPermaBanService
import org.cryptotrader.security.library.service.IpBanService
import org.cryptotrader.security.library.service.SecurityThreatService
import org.cryptotrader.security.library.service.entity.BannedIpAddressEntityService
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.beans.factory.ObjectProvider
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.cryptotrader.security.library.event.SecurityEventLogger
import org.cryptotrader.security.library.model.BanType
import org.springframework.beans.factory.InitializingBean
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import java.util.Base64

@AutoConfiguration
@EnableConfigurationProperties(SecurityPropertiesConfig::class)
@PropertySource(
    value = ["classpath:application-secure.yml"],
    factory = YamlPropertySourceFactory::class
)
open class SecurityAutoConfig {

    @Configuration
    @ConditionalOnClass(LocalContainerEntityManagerFactoryBean::class)
    @ConditionalOnBean(type = ["javax.sql.DataSource"])
    @EnableJpaRepositories(basePackages = ["org.cryptotrader.security.library.repository"])
    @EntityScan(basePackages = ["org.cryptotrader.security.library.entity", "org.cryptotrader.api.library.entity"])
    open class SecurityJpaConfig

    @Bean
    @ConditionalOnMissingBean
    open fun ipBanService(
        properties: SecurityPropertiesConfig,
        serviceProvider: ObjectProvider<BannedIpAddressEntityService>
    ): IpBanManager {
        val tempBanService = InMemoryIpBanService()
        val permaBanService: IpPermaBanService? = serviceProvider.getIfAvailable()?.let { IpPermaBanService(it) }
        // If we can't create a bean, we use the more sturdy in-memory ban
        // service.
        if (permaBanService == null) {
            properties.bans.denylist.forEach { ipOrCidr ->
                tempBanService.ban(ipOrCidr, BanType.PERMA)
            }
            return tempBanService
        }
        // If we can create a bean, we check both services.
        val banService = IpBanService(tempBanService, permaBanService)
        properties.bans.denylist.forEach { ipOrCidr ->
            banService.ban(ipOrCidr, BanType.PERMA)
        }
        return banService
    }

    @Bean
    @ConditionalOnMissingBean
    open fun encryptionService(properties: SecurityPropertiesConfig): EncryptionService {
        val keysetPath = properties.encryption?.tink?.keysetPath ?: properties.crypto.tink.keysetPath
        return EncryptionService(keysetPath)
    }

    @Bean
    @ConditionalOnBean(BannedIpAddressEntityService::class)
    open fun securityEventLogger(entityService: BannedIpAddressEntityService): SecurityEventLogger {
        return SecurityEventLogger(entityService)
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(IpBanService::class)
    open fun handleSecurityThreat(
        ipBanService: IpBanService,
        properties: SecurityPropertiesConfig
    ): SecurityThreatService = SecurityThreatService(ipBanService, properties.http.blockResponseCode)

    @Bean
    @ConditionalOnBean(IpBanFilter::class)
    open fun ipBanFilterRegistration(
        filter: IpBanFilter,
        properties: SecurityPropertiesConfig
    ): FilterRegistrationBean<IpBanFilter> {
        val registration = FilterRegistrationBean(filter)
        registration.order = -100
        registration.setName("securityIpBanFilter")
        registration.isEnabled = properties.bans.enabled
        return registration
    }

    @Bean
    open fun configureEntityEncryption(encryptionService: EncryptionService): InitializingBean {
        return InitializingBean {
            EncryptedKey.setEncrypterDelegate(object : KeyEncrypter {
                override fun encrypt(key: String): String {
                    val encryptedBytes: ByteArray = encryptionService.encrypt(key.toByteArray(Charsets.UTF_8))
                    return Base64.getEncoder().encodeToString(encryptedBytes)
                }

                override fun decrypt(key: String): String {
                    val decodedBytes: ByteArray = Base64.getDecoder().decode(key)
                    val decryptedBytes: ByteArray = encryptionService.decrypt(decodedBytes)
                    return String(decryptedBytes, Charsets.UTF_8)
                }
            })
        }
    }
}
