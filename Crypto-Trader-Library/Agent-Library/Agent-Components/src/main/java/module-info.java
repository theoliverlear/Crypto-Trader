module org.cryptotrader.agent.library.components {
    requires kotlin.stdlib;
    requires kotlin.reflect;
    requires spring.context;
    requires spring.boot;
    requires spring.boot.autoconfigure;
    requires java.sql;
    requires java.net.http;
    requires org.cryptotrader.agent.library.config;
    requires org.cryptotrader.agent.library.models;
    requires org.cryptotrader.agent.library.communication;
    requires spring.security.core;
    requires spring.ai.mcp;
    requires spring.ai.model;
    requires mcp.annotations;
    requires spring.data.commons;
    requires spring.data.jpa;
    requires spring.web;
    requires jakarta.persistence;
    requires org.jsoup;
    requires org.apache.httpcomponents.httpclient;
    requires org.apache.httpcomponents.httpcore;

    exports org.cryptotrader.agent.library.component;
    exports org.cryptotrader.agent.library.component.config;
    opens org.cryptotrader.agent.library.component to spring.core, spring.beans, spring.context;
    opens org.cryptotrader.agent.library.component.config;
}