package org.cryptotrader.api.library.config;


import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration("apiLibrarySecurityConfig")
@EnableWebSecurity
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@ConditionalOnProperty(name = "cryptotrader.api.security.enabled", havingValue = "true", matchIfMissing = true)
public class SecurityConfig {
    //==============================-Beans-===================================

    //------------------------Security-Filter-Chain---------------------------
    @Bean
    @ConditionalOnMissingBean(SecurityFilterChain.class)
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Scope this filter chain to only API docs and actuator endpoints to avoid overlapping with
        // application-level SecurityFilterChain definitions.
//        http.securityMatcher(
//                "/swagger-ui/**",
//                "/v3/api-docs/**",
//                "/v3/api-docs.yaml",
//                "/actuator/health",
//                "/actuator/info",
//                // Any endpoint
//                "/**"
//        );
//        http.authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll())
//           .csrf(AbstractHttpConfigurer::disable);

        http.securityMatcher(
                "/swagger-ui/**",
                "/v3/api-docs/**",
                "/v3/api-docs.yaml",
                "/actuator/**"
        );
        http.authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll())
           .csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }
    //--------------------------Password-Encoder------------------------------
    @Bean
    @ConditionalOnMissingBean(BCryptPasswordEncoder.class)
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}