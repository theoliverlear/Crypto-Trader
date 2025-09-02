module org.cryptotrader.logging {
    requires spring.boot.autoconfigure;
    requires static lombok;
    requires spring.boot;
    requires spring.web;
    requires spring.context;
    requires org.slf4j;
    
    exports org.cryptotrader.logging.config;
    exports org.cryptotrader.logging;
}