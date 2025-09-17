open module org.cryptotrader.security {
    requires jakarta.persistence;
    requires spring.boot;
    requires spring.boot.autoconfigure;
    requires spring.context;
    requires spring.beans;
    requires spring.core;
    requires spring.web;
    requires spring.security.web;
    requires spring.security.config;
    requires spring.security.core;
    requires spring.data.jpa;
    requires static lombok;
    requires com.google.crypto.tink;
    requires inet.ipaddr;
    requires org.cryptotrader.api.library.models;
    requires kotlin.stdlib;

    exports org.cryptotrader.security;
}