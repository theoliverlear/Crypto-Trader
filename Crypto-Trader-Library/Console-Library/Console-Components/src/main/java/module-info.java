open module org.cryptotrader.console.library.components {
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires static lombok;
    requires static org.slf4j;
    requires org.apache.httpcomponents.httpclient;
    requires org.apache.httpcomponents.httpcore;
    requires spring.beans;
    requires spring.context;
    requires spring.core;
    requires spring.web;
    requires spring.jdbc;
    requires kotlin.stdlib;
    requires spring.boot.autoconfigure;
    requires spring.cloud.stream;
    requires org.cryptotrader.universal.library.components;
    requires org.cryptotrader.console.library.communication;
    requires org.cryptotrader.console.library.services;
    
    exports org.cryptotrader.console.library.component;
}