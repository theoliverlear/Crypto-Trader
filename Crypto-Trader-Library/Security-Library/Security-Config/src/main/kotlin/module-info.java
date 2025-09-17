open module org.cryptotrader.security.library.config {
    requires kotlin.stdlib;
    requires spring.boot;
    requires spring.boot.autoconfigure;
    requires spring.context;
    requires spring.beans;
    requires spring.data.jpa;
    requires spring.web;

    requires org.cryptotrader.security.library.repositories;
    requires org.cryptotrader.security.library.events;
    requires org.cryptotrader.security.library.infrastructure;
    requires org.cryptotrader.security.library.services;

    exports org.cryptotrader.security.library.config;
}
