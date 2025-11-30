module org.cryptotrader.data.library.repositories {
    requires org.cryptotrader.data.library.models;
    requires spring.data.commons;
    requires spring.data.jpa;
    requires kotlin.stdlib;
    requires spring.core;
    requires spring.beans;
    requires spring.context;
    requires jakarta.persistence;

    opens org.cryptotrader.data.library.repository to spring.beans, spring.context, spring.core, spring.data.commons, spring.data.jpa;
    exports org.cryptotrader.data.library.repository;
}