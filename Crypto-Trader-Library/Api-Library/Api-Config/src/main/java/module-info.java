open module org.cryptotrader.api.library.config {
    requires org.apache.httpcomponents.httpclient;
    requires spring.beans;
    requires spring.boot.autoconfigure;
    requires spring.cloud.stream;
    requires spring.context;
    requires spring.security.config;
    requires spring.security.crypto;
    requires spring.security.web;
    requires spring.web;
    requires spring.webmvc;
    requires spring.integration.core;
    requires spring.messaging;
    requires spring.core;
    requires spring.boot;
    requires org.jetbrains.annotations;

    exports org.cryptotrader.api.library.config;
}