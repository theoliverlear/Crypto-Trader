open module org.cryptotrader.security.library.config {
    requires kotlin.stdlib;
    requires spring.boot;
    requires spring.boot.autoconfigure;
    requires spring.context;
    requires spring.beans;
    requires spring.data.jpa;
    requires spring.web;
    requires org.apache.tomcat.embed.core;
    requires spring.security.config;
    requires spring.security.web;
    requires spring.security.core;

    requires org.cryptotrader.security.library.repositories;
    requires org.cryptotrader.security.library.events;
    requires org.cryptotrader.security.library.infrastructure;
    requires org.cryptotrader.security.library.services;
    requires org.cryptotrader.security.library.models;
    requires org.cryptotrader.universal.library.models;

    exports org.cryptotrader.security.library.config;
}
