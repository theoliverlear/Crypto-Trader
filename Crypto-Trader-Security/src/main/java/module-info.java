open module org.cryptotrader.security {
    requires jakarta.persistence;
    requires spring.boot;
    requires spring.boot.autoconfigure;
    requires spring.context;
    requires spring.beans;
    requires spring.core;
    requires spring.web;
    requires spring.security.web;
    requires spring.security.config;
    requires spring.security.core;
    requires spring.data.jpa;
    requires micrometer.core;

    exports org.cryptotrader.security.jpa;
    exports org.cryptotrader.security.config;
    exports org.cryptotrader.security.net;
    exports org.cryptotrader.security.events;
    exports org.cryptotrader.security.ban;
}