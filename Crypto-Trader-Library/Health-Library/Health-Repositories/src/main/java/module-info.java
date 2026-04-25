open module org.cryptotrader.health.library.repositories {
    requires org.cryptotrader.health.library.models;
    requires spring.data.jpa;
    requires spring.data.commons;
    requires jakarta.persistence;

    exports org.cryptotrader.health.library.repository;
}
