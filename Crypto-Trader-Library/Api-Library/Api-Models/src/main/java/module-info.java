open module org.cryptotrader.api.models {
    requires static com.fasterxml.jackson.annotation;
    requires static jakarta.persistence;

    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires static lombok;
    requires org.slf4j;
    requires spring.beans;
    requires spring.context;
    requires spring.security.crypto;
    requires spring.web;
    requires org.hibernate.orm.core;

    exports org.cryptotrader.entity;
    exports org.cryptotrader.entity.currency;
    exports org.cryptotrader.entity.currency.builder;
    exports org.cryptotrader.entity.currency.builder.models;
    exports org.cryptotrader.entity.news;
    exports org.cryptotrader.entity.news.builder;
    exports org.cryptotrader.entity.news.builder.models;
    exports org.cryptotrader.entity.portfolio;
    exports org.cryptotrader.entity.portfolio.builder;
    exports org.cryptotrader.entity.portfolio.builder.models;
    exports org.cryptotrader.entity.prediction;
    exports org.cryptotrader.entity.prediction.builder;
    exports org.cryptotrader.entity.prediction.builder.models;
    exports org.cryptotrader.entity.trade;
    exports org.cryptotrader.entity.training;
    exports org.cryptotrader.entity.training.specs;
    exports org.cryptotrader.entity.training.builder;
    exports org.cryptotrader.entity.training.builder.models;
    exports org.cryptotrader.entity.user;
    exports org.cryptotrader.entity.user.admin;
    exports org.cryptotrader.entity.user.builder;
    exports org.cryptotrader.entity.user.builder.models;
    exports org.cryptotrader.entity.vendor;

    exports org.cryptotrader.model;
    exports org.cryptotrader.model.http;
    exports org.cryptotrader.model.trade;
}