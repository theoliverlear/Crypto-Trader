package org.cryptotrader.api.controller

import org.cryptotrader.api.service.HoneypotService
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.env.Environment
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Honeypot Controller")
@TestPropertySource(properties = [
    "spring.web.resources.add-mappings=false",
    "spring.mvc.pathmatch.matching-strategy=path_pattern_parser"
])
class HoneypotControllerTest {

    @Autowired
    private lateinit var env: Environment

    @Autowired
    private lateinit var mockMvc: MockMvc
    
    @MockitoSpyBean
    private lateinit var honeypotService: HoneypotService

    @Test
    @DisplayName("All configured honeypot paths are non-empty and are handled by the controller")
    fun allConfiguredPathsAreHandledByController() {
        val raw = env.getProperty("security.honeypot.paths") ?: ""
        val paths = raw.split(',')
            .map { it.trim() }
            .filter { it.isNotEmpty() }
            .filter { !it.contains("*") }
            .distinct()
        
        assertFalse(paths.isEmpty()) { "Expected at least one honeypot path from configuration, but none were found." }

        for (p in paths) {
            clearInvocations(honeypotService)
            mockMvc.perform(get(p))
                .andExpect(status().isNotFound)
            verify(honeypotService, atLeastOnce()).captureHoneypot(
                ArgumentMatchers.any())
        }
    }
}