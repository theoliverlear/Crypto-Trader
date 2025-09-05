open module org.cryptotrader.api.config {
    requires spring.boot.autoconfigure;
    requires spring.context;
    requires spring.data.jpa;
    requires spring.boot;
    requires static lombok;
    requires spring.beans;
    requires java.sql;
    requires spring.web;
    requires org.slf4j;
    requires spring.core;
    requires spring.aop;
    requires spring.tx;
    requires spring.security.config;
    requires spring.security.crypto;
    requires spring.security.web;
    requires spring.webmvc;
    requires org.apache.httpcomponents.httpclient;

    exports org.cryptotrader.api.config;
}