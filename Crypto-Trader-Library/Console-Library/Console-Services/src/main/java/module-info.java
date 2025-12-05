module org.cryptotrader.console.library.services {
    requires java.sql;
    requires static lombok;
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
    requires spring.data.commons;
    requires org.cryptotrader.console.library.communication;
    
    exports org.cryptotrader.console.library.services;
}