open module org.cryptotrader.logging.library.services {
    requires spring.context;
    requires spring.beans;
    requires spring.tx;
    requires spring.data.jpa;
    requires static lombok;
    requires org.slf4j;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires org.cryptotrader.logging.library.models;
    requires org.cryptotrader.logging.library.events;
    requires org.cryptotrader.logging.library.repositories;

    exports org.cryptotrader.logging.library.service;
}
