open module org.cryptotrader.data {
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
    requires org.slf4j;
    requires spring.core;
    requires spring.aop;
    requires spring.tx;
    requires spring.security.config;
    requires spring.security.crypto;
    requires spring.security.web;

    exports org.cryptotrader.data;
    exports org.cryptotrader.data.adapter;
    exports org.cryptotrader.data.config;
    exports org.cryptotrader.data.controller;
    exports org.cryptotrader.data.service;
}