module org.cryptotrader.api.library.repositories {
    requires kotlin.stdlib;
    requires org.cryptotrader.api.library.models;
    requires spring.data.jpa;
    requires spring.data.commons;
    requires spring.core;
    requires spring.beans;
    requires spring.context;
    requires jakarta.persistence;
    requires org.cryptotrader.data.library.models;

    opens org.cryptotrader.api.library.repository to spring.beans, spring.context, spring.core, spring.data.commons, spring.data.jpa;
    exports org.cryptotrader.api.library.repository;
}