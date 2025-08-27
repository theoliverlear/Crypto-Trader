package org.cryptotrader.admin.config;

import org.cryptotrader.desktop.component.config.SpringContext;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@ComponentScan(basePackages = {"org.cryptotrader.admin", 
                               "org.cryptotrader.desktop.component"})
@Import(SpringContext.class)
public class SpringBootConfig {
    
}
