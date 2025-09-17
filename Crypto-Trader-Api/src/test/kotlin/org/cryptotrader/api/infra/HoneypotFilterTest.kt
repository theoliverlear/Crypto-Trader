package org.cryptotrader.api.infra

import org.cryptotrader.api.config.WebSocketConfig
import org.cryptotrader.api.service.HoneypotService
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.Mockito.atLeastOnce
import org.mockito.Mockito.clearInvocations
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.PropertySource
import org.springframework.core.env.Environment
import org.springframework.http.ResponseEntity
import org.springframework.test.context.TestPropertySource
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.cryptotrader.security.library.config.YamlPropertySourceFactory
import org.springframework.context.annotation.FilterType

@WebMvcTest(
    excludeAutoConfiguration = [
        DataSourceAutoConfiguration::class,
        HibernateJpaAutoConfiguration::class,
        JpaRepositoriesAutoConfiguration::class
    ],
    excludeFilters = [
        org.springframework.context.annotation.ComponentScan.Filter(
            type = FilterType.ASSIGNABLE_TYPE,
            classes = [WebSocketConfig::class]
        )
    ]
)
@TestPropertySource(properties = [
    "spring.web.resources.add-mappings=false", 
    "cryptotrader.api.jpa.enabled=false",      
    "cryptotrader.api.websocket.enabled=false",
    "spring.autoconfigure.exclude=org.cryptotrader.security.library.config.SecurityAutoConfig"
])
class HoneypotFilterTest {

    @TestConfiguration
    @PropertySource(value = ["classpath:application-secure.yml"], factory = YamlPropertySourceFactory::class)
    @Import(HoneypotFilter::class)
    open class TestConfig

    @Autowired lateinit var mvc: MockMvc

    @Autowired lateinit var env: Environment

    @org.springframework.boot.test.mock.mockito.MockBean
    lateinit var honeypotService: HoneypotService

    @Test
    fun `all configured honeypot paths are intercepted and service invoked`() {
        `when`(honeypotService.captureHoneypot(any())).thenReturn(ResponseEntity.notFound().build())

        val raw = env.getProperty("security.honeypot.paths") ?: ""
        val paths = raw.split(',')
            .map { it.trim() }
            .filter { it.isNotEmpty() }
            .filter { !it.contains("*") } // skip wildcard entries like /**/.env
            .distinct()

        assertFalse(paths.isEmpty()) { "Expected at least one honeypot path from configuration, but none were found." }

        paths.forEach { path ->
            clearInvocations(honeypotService)
            mvc.perform(get(path)).andExpect(status().isNotFound)
            verify(honeypotService, atLeastOnce()).captureHoneypot(any())
        }
    }
}