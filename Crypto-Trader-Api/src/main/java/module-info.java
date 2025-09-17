open module org.cryptotrader.api {
    requires kotlin.stdlib;
    requires org.cryptotrader.api.library;
    requires com.sigwarthsoftware.springboot.websocket;

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
    requires jakarta.xml.bind;
    requires jakarta.activation;

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
    requires org.apache.tomcat.embed.core;
    requires org.apache.tomcat.embed.websocket;

    requires spring.webmvc;
    requires org.cryptotrader.api.library.models;
    requires org.cryptotrader.api.library.communication;
    requires org.cryptotrader.api.library.services;
    requires org.cryptotrader.api.library.components;
    requires org.cryptotrader.api.library.repositories;
    requires org.cryptotrader.api.library.events;
    requires io.swagger.v3.oas.annotations;
    requires org.cryptotrader.docs;
    
    requires org.cryptotrader.security.library.services;
    requires org.cryptotrader.security.library.config;

    exports org.cryptotrader.api;
    exports org.cryptotrader.api.config;
    exports org.cryptotrader.api.controller;
    exports org.cryptotrader.api.controller.websocket;
}