open module org.cryptotrader.health.library.components {
    requires org.cryptotrader.health.library.models;
    requires org.cryptotrader.health.library.services;
    requires spring.context;
    requires org.slf4j;
    requires kotlin.stdlib;

    exports org.cryptotrader.health.library.component;
}
