package org.cryptotrader.data;

import org.cryptotrader.test.CryptoTraderTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.cryptotrader.api.library.component.CurrencyDataRetriever;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(
        classes = TestApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.NONE,
        properties = {
                "spring.autoconfigure.exclude=org.cryptotrader.docs.autoconfigure.CryptoTraderDocsAutoConfiguration,org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration,org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration,org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration",
                "docs.autoconfigure.enabled=false"
        }
)
class ConfigLoadTest extends CryptoTraderTest {

    @MockitoBean
    private CurrencyDataRetriever currencyDataRetriever;

    @Autowired
    private Environment environment;

    @Test
    void loadsApplicationYaml_propertiesPresent() {
        assertThat(environment.getProperty("spring.application.name")).isEqualTo("crypto-trader-data");
        assertThat(environment.getProperty("server.port")).isEqualTo("8085");
    }
}
