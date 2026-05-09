open module org.cryptotrader.simulator {
    requires kotlin.stdlib;
    requires spring.boot;
    requires spring.boot.autoconfigure;
    requires spring.context;
    requires org.cryptotrader.simulator.library.config;
    requires org.cryptotrader.simulator.library.services;
    requires org.cryptotrader.simulator.library.events;
    requires org.cryptotrader.logging.library.config;
}