open module org.cryptotrader.health.library.services {
    requires org.cryptotrader.health.library.models;
    requires org.cryptotrader.health.library.repositories;
    requires spring.beans;
    requires spring.context;
    requires spring.tx;
    requires spring.web;
    requires java.net.http;
    requires static lombok;
    requires org.slf4j;

    exports org.cryptotrader.health.library.service;
}
