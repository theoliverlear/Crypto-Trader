module org.cryptotrader.simulator.library.services {
    requires kotlin.stdlib;
    requires java.sql;

    requires org.cryptotrader.universal.library.models;
    requires org.cryptotrader.api.library.models;
    requires org.cryptotrader.data.library.models;
    requires org.cryptotrader.data.library.repositories;
    requires org.cryptotrader.simulator.library.scripts;

    requires spring.boot.starter.data.jpa;
    requires spring.beans;
    requires spring.context;

    exports org.cryptotrader.simulator.library.services;
}