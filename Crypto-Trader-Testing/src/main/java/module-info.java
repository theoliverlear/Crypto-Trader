module org.cryptotrader.test {
    requires static lombok;

    requires org.slf4j;

    requires org.junit.jupiter.api;
    
    requires spring.beans;
    requires spring.context;
    requires spring.web;

    requires static jakarta.persistence;
    requires static org.hibernate.orm.core;
    requires org.jetbrains.annotations;
    requires org.mockito.junit.jupiter;

    exports org.cryptotrader.test;
    exports org.cryptotrader.test.logging;
}