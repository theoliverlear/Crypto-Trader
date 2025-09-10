package org.cryptotrader.data;

import org.cryptotrader.test.CryptoTraderTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = CryptoTraderDataApplication.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
class ConfigLoadTest extends CryptoTraderTest {

    @Autowired
    private Environment environment;

    @Test
    void loadsApplicationYaml_propertiesPresent() {
        assertThat(environment.getProperty("spring.application.name")).isEqualTo("crypto-trader-data");
        assertThat(environment.getProperty("server.port")).isEqualTo("8085");
    }
}
