module org.cryptotrader.data {
    requires spring.boot.autoconfigure;
    requires spring.context;
    requires spring.data.jpa;
    requires org.cryptotrader.api.components;
    requires spring.boot;
    requires static lombok;
    requires org.cryptotrader.api.repositories;
    requires org.cryptotrader.api.services;
    requires spring.beans;
    requires org.cryptotrader.api.models;
    requires org.cryptotrader.api.communication;
    requires java.sql;
    requires spring.web;
}