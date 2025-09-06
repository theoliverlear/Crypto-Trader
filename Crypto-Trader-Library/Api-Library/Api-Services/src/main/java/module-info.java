open module org.cryptotrader.api.services {
    requires java.sql;
    requires static lombok;
    requires org.cryptotrader.api.communication;
    requires org.cryptotrader.api.components;
    requires org.cryptotrader.api.models;
    requires org.cryptotrader.api.repositories;
    requires spring.beans;
    requires spring.context;
    requires spring.jdbc;
    requires spring.tx;
    requires spring.web;
    requires spring.boot.autoconfigure;
    requires tomcat.embed.core;
    requires org.slf4j;
    requires kotlin.stdlib;
    
    exports org.cryptotrader.api.services;
    exports org.cryptotrader.api.services.models;
    exports org.cryptotrader.api.services.config;
}