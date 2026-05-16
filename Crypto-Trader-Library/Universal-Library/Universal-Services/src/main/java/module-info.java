open module org.cryptotrader.universal.library.services {
    requires kotlin.stdlib;
    requires org.cryptotrader.universal.library.models;
    requires spring.data.jpa;
    requires org.slf4j;

    exports org.cryptotrader.universal.library.services;
}