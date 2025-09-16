package org.cryptotrader.api.library.config;


import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration("apiLibrarySecurityConfig")
@EnableWebSecurity
@ConditionalOnProperty(name = "cryptotrader.api.security.enabled", havingValue = "true", matchIfMissing = true)
public class SecurityConfig {
    //==============================-Beans-===================================

    //------------------------Security-Filter-Chain---------------------------
    @Bean
    @ConditionalOnMissingBean(SecurityFilterChain.class)
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorize -> {
            authorize.requestMatchers(
                            "/swagger-ui/**",
                            "/v3/api-docs/**",
                            "/v3/api-docs.yaml",
                            "/actuator/health",
                            "/actuator/info"
                    )
                     .permitAll().anyRequest().permitAll();
        }).csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }
    //--------------------------Password-Encoder------------------------------
    @Bean
    @ConditionalOnMissingBean(BCryptPasswordEncoder.class)
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}