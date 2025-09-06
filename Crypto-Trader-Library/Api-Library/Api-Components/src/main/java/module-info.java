open module org.cryptotrader.api.components {
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires static lombok;
    requires static org.slf4j;
    requires org.apache.httpcomponents.httpclient;
    requires org.apache.httpcomponents.httpcore;
    requires org.cryptotrader.api.communication;
    requires org.cryptotrader.api.models;
    requires spring.beans;
    requires spring.context;
    requires spring.core;
    requires spring.web;
    requires spring.jdbc;
    requires kotlin.stdlib;

    exports org.cryptotrader.component;
    exports org.cryptotrader.component.config;
}