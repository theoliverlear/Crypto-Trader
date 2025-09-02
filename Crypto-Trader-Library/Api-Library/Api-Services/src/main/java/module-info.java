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
    requires tomcat.embed.core;
    requires org.slf4j;
    
    exports org.cryptotrader.api.services;
    exports org.cryptotrader.api.services.models;
}