open module org.cryptotrader.repositories {
    requires org.cryptotrader.contact.models;
    requires kotlin.stdlib;
    requires spring.data.jpa;
    requires spring.data.commons;
    requires spring.core;
    requires spring.beans;
    requires spring.context;
    requires jakarta.persistence;
    
    exports org.cryptotrader.repository;
}