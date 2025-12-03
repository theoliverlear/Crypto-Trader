open module org.cryptotrader.engine {
    requires spring.core;
    requires spring.beans;
    requires spring.context;
    requires spring.aop;

    requires spring.boot.autoconfigure;
    requires spring.data.jpa;
    requires spring.boot;
    requires static lombok;
    requires org.slf4j;
    requires org.cryptotrader.api.library.services;
    requires org.cryptotrader.api.library.models;
    requires spring.security.config;
    requires spring.security.crypto;
    requires spring.security.web;
    requires kotlin.stdlib;
    requires org.cryptotrader.health;

    requires jakarta.xml.bind;
    requires jakarta.activation;
    requires org.apache.tomcat.embed.core;
    requires org.apache.tomcat.embed.websocket;
    requires spring.tx;

    exports org.cryptotrader.engine;
    exports org.cryptotrader.engine.services;
}