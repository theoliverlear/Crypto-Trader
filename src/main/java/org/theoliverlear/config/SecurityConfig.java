package org.theoliverlear.config;

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

    //----------------------------SSL-Context---------------------------------
    @Bean
    public SSLContext sslContext(ResourceLoader sslLoader) {
        SSLContext sslContext = null;
        try {
            // We find the SSL password.
            String keyStorePassword = System.getenv("CRYPTO_TRADER_SSL_PW");
            char[] keyStorePasswordCharArray = keyStorePassword.toCharArray();
            // We load the file as a resource and then as a file.
            Resource keyResource = sslLoader.getResource("classpath:ssl/keystore.p12");
            File keyStoreFile = keyResource.getFile();
            // We create a generic key store of type PKCS12 and load the file.
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            keyStore.load(new FileInputStream(keyStoreFile), keyStorePasswordCharArray);
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            // We initialize the key manager factory with the key store and
            // password.
            keyManagerFactory.init(keyStore, keyStorePasswordCharArray);
            // We initialize the SSL context with the key manager factory and
            // a null trust manager and a secure random number generator.
            sslContext = SSLContext.getInstance("TLS");
            sslContext.init(keyManagerFactory.getKeyManagers(), null, new SecureRandom());
        } catch (KeyStoreException | IOException | NoSuchAlgorithmException |
                 CertificateException | UnrecoverableKeyException |
                 KeyManagementException ex) {
            // If anything fails, the server will not start.
            throw new RuntimeException(ex);
        }
        return sslContext;
    }
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
