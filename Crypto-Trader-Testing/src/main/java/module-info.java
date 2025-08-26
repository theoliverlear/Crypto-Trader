module org.cryptotrader.test {
    // Compile-time only for Lombok annotations
    requires static lombok;

    // Logging API used by testing utilities
    requires org.slf4j;

    requires org.junit.jupiter.api;
    
    // Spring bits available to testing helpers (present in POM)
    requires spring.beans;
    requires spring.context;
    requires spring.web;

    // Optional compile-time presence for JPA/Hibernate types referenced by helpers
    requires static jakarta.persistence;
    requires static org.hibernate.orm.core;
    
    exports org.cryptotrader.test;
    exports org.cryptotrader.test.logging;
}