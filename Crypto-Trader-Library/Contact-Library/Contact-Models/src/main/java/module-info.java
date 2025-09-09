module org.cryptotrader.contact.models {
    requires kotlin.stdlib;
    requires jakarta.persistence;
    requires org.cryptotrader.api.models;
    requires static lombok;
    requires spring.data.jpa;
    requires spring.data.commons;
    requires spring.core;
    requires spring.beans;
    requires spring.context;

    exports org.cryptotrader.contact.entity;
}