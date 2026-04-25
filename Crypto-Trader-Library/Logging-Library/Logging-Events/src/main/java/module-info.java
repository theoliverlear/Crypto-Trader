open module org.cryptotrader.logging.library.events {
    requires kotlin.stdlib;
    requires spring.context;
    requires spring.beans;
    requires org.slf4j;
    requires org.cryptotrader.universal.library.components;

    exports org.cryptotrader.logging.library.events;
    exports org.cryptotrader.logging.library.events.publisher;
}
