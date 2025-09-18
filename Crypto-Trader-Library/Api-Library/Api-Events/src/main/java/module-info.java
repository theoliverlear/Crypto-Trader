open module org.cryptotrader.api.library.events {
    requires kotlin.stdlib;
    requires spring.context;
    requires spring.beans;
    requires spring.core;
    requires org.cryptotrader.api.library.models;
    requires org.cryptotrader.api.library.components;

    exports org.cryptotrader.api.library.events;
    exports org.cryptotrader.api.library.events.publisher;
}