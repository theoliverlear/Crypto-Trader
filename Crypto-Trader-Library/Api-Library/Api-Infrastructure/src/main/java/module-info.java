open module org.cryptotrader.api.library.infrastructure {
    requires kotlin.stdlib;
    requires spring.context;
    requires spring.beans;
    requires spring.web;
    requires spring.websocket;
    requires org.slf4j;
    requires jakarta.servlet;

    requires org.cryptotrader.api.library.services;
    requires org.cryptotrader.api.library.models;

    exports org.cryptotrader.api.library.infrastructure;
}
