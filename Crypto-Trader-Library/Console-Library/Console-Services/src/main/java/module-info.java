open module org.cryptotrader.console.library.services {
    requires java.sql;
    requires java.net.http;
    requires static lombok;
    requires spring.aop;
    requires spring.beans;
    requires spring.context;
    requires spring.jdbc;
    requires spring.tx;
    requires spring.web;
    requires spring.boot.autoconfigure;
    requires org.apache.tomcat.embed.core;
    requires org.slf4j;
    requires kotlin.stdlib;
    requires jjwt.api;
    requires com.auth0.jwt;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.annotation;
    requires spring.data.commons;
    requires org.cryptotrader.console.library.communication;
    requires org.cryptotrader.api.library.communication;
    requires org.cryptotrader.console.library.models;
    requires org.cryptotrader.data.library.services;
    requires org.cryptotrader.data.library.components;
    requires org.cryptotrader.data.library.models;
    requires org.cryptotrader.api.library.services;
    requires org.cryptotrader.api.library.models;
    requires org.cryptotrader.console.library.components;
    
    exports org.cryptotrader.console.library.services;
    exports org.cryptotrader.console.library.services.models;
}