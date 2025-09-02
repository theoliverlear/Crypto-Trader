open module org.cryptotrader.api {

    requires org.cryptotrader.api.library;
    
    requires spring.boot;
    requires spring.boot.autoconfigure;

    requires spring.context;
    requires spring.core;
    requires spring.beans;
    requires spring.aop;
    requires spring.web;
    requires spring.tx;
    requires spring.data.jpa;

    requires jakarta.persistence;


    requires spring.security.config;
    requires spring.security.web;
    requires spring.security.crypto;


    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;

    requires org.apache.httpcomponents.httpclient;

    requires spring.jdbc;

    requires org.slf4j;

    requires org.apache.httpcomponents.httpcore;

    requires org.hibernate.orm.core;


    requires static lombok;
    requires tomcat.embed.core;
    requires tomcat.embed.websocket;
    
    requires org.cryptotrader.test;
    requires spring.webmvc;
    requires org.cryptotrader.api.models;
    requires org.cryptotrader.api.communication;
    requires org.cryptotrader.api.services;

    exports org.cryptotrader.api;
}