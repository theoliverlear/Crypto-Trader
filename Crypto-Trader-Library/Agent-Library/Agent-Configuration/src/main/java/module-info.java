module org.cryptotrader.agent.library.config {
    requires kotlin.stdlib;
    requires kotlin.reflect;
    requires spring.context;
    requires spring.boot;
    requires spring.boot.autoconfigure;
    requires spring.security.config;
    requires spring.security.web;

    exports org.cryptotrader.agent.library.config;
    opens org.cryptotrader.agent.library.config;
}