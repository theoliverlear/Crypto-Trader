open module org.cryptotrader.api.library.infrastructure {
    requires kotlin.stdlib;
    requires spring.context;
    requires spring.beans;
    requires spring.web;
    requires spring.websocket;
    requires spring.boot.autoconfigure;
    requires org.slf4j;
    requires org.apache.tomcat.embed.core;
    
    requires org.cryptotrader.api.library.services;
    requires org.cryptotrader.api.library.models;
    requires org.cryptotrader.api.library.scripts;
    requires org.cryptotrader.api.library.components;
    requires com.auth0.jwt;

    exports org.cryptotrader.api.library.infrastructure;
    exports org.cryptotrader.api.library.infrastructure.dpop;
}
