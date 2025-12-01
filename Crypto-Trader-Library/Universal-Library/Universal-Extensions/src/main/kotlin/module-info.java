module org.cryptotrader.universal.library.extensions {
    requires kotlin.stdlib;
    requires spring.context;
    requires spring.beans;
    requires spring.web;
    requires spring.websocket;
    requires spring.boot.autoconfigure;
    requires org.slf4j;
    requires org.apache.tomcat.embed.core;
    requires org.cryptotrader.universal.library.models;
    requires com.auth0.jwt;
    
    exports org.cryptotrader.universal.library.extension.servlet;
    exports org.cryptotrader.universal.library.extension.string;
}