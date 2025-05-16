package org.theoliverlear.config;
//=================================-Imports-==================================
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;

@Configuration
public class SecurityConfig {
    //==============================-Beans-===================================

    //------------------------Security-Filter-Chain---------------------------
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // HttpSecurity is the builder object used to create the security
        // filter chain. We authorize all requests and disable CSRF.
        http.authorizeRequests()
                .anyRequest().permitAll()
                .and()
                .csrf()
                .disable();
        return http.build();
    }
    //--------------------------Password-Encoder------------------------------
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
