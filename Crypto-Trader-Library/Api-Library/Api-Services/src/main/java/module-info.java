open module org.cryptotrader.api.library.services {
    requires java.sql;
    requires static lombok;
    requires org.cryptotrader.api.library.communication;
    requires org.cryptotrader.api.library.components;
    requires org.cryptotrader.api.library.models;
    requires org.cryptotrader.api.library.repositories;
    requires spring.beans;
    requires spring.context;
    requires spring.jdbc;
    requires spring.tx;
    requires spring.web;
    requires spring.boot.autoconfigure;
    requires tomcat.embed.core;
    requires org.slf4j;
    requires kotlin.stdlib;
    
    exports org.cryptotrader.api.library.services.models;
    exports org.cryptotrader.api.library.services.config;
    exports org.cryptotrader.api.library.services;
}