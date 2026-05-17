open module org.cryptotrader.universal.library.services {
    requires kotlin.stdlib;
    requires org.cryptotrader.universal.library.models;
    requires spring.data.jpa;
    requires org.slf4j;
    requires spring.tx;

    exports org.cryptotrader.universal.library.services;
}
