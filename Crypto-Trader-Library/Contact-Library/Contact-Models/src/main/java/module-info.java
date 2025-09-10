module org.cryptotrader.contact.library.models {
    requires kotlin.stdlib;
    requires jakarta.persistence;
    requires org.cryptotrader.api.library.models;
    requires static lombok;
    requires spring.data.jpa;
    requires spring.data.commons;
    requires spring.core;
    requires spring.beans;
    requires spring.context;

    exports org.cryptotrader.contact.library.entity;
}