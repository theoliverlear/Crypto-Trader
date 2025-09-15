open module org.cryptotrader.api.library.models {
    requires static com.fasterxml.jackson.annotation;
    requires static jakarta.persistence;
    requires kotlin.stdlib;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires static lombok;
    requires org.slf4j;
    requires spring.beans;
    requires spring.context;
    requires spring.security.crypto;
    requires spring.web;
    requires org.hibernate.orm.core;

    exports org.cryptotrader.api.library.entity;
    exports org.cryptotrader.api.library.entity.currency;
    exports org.cryptotrader.api.library.entity.currency.builder;
    exports org.cryptotrader.api.library.entity.currency.builder.models;
    exports org.cryptotrader.api.library.entity.news;
    exports org.cryptotrader.api.library.entity.news.builder;
    exports org.cryptotrader.api.library.entity.news.builder.models;
    exports org.cryptotrader.api.library.entity.portfolio;
    exports org.cryptotrader.api.library.entity.portfolio.builder;
    exports org.cryptotrader.api.library.entity.portfolio.builder.models;
    exports org.cryptotrader.api.library.entity.prediction;
    exports org.cryptotrader.api.library.entity.prediction.builder;
    exports org.cryptotrader.api.library.entity.prediction.builder.models;
    exports org.cryptotrader.api.library.entity.trade;
    exports org.cryptotrader.api.library.entity.training;
    exports org.cryptotrader.api.library.entity.training.specs;
    exports org.cryptotrader.api.library.entity.training.builder;
    exports org.cryptotrader.api.library.entity.training.builder.models;
    exports org.cryptotrader.api.library.entity.user;
    exports org.cryptotrader.api.library.entity.user.admin;
    exports org.cryptotrader.api.library.entity.user.builder;
    exports org.cryptotrader.api.library.entity.user.builder.models;
    exports org.cryptotrader.api.library.entity.vendor;

    exports org.cryptotrader.api.library.model;
    exports org.cryptotrader.api.library.model.http;
    exports org.cryptotrader.api.library.model.trade;
    exports org.cryptotrader.api.library.model.currency;
}