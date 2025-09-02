module org.cryptotrader.api.repositories {
    exports org.cryptotrader.repository;

    requires org.cryptotrader.api.models;
    requires spring.data.jpa;
    requires spring.data.commons;

    opens org.cryptotrader.repository to spring.core, spring.beans, spring.context, spring.data.commons, spring.data.jpa;
    requires jakarta.persistence;

}