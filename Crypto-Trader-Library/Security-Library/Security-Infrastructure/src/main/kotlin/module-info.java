open module org.cryptotrader.security.library.infrastructure {
    requires kotlin.stdlib;
    requires spring.web;
    requires spring.core;
    requires org.apache.tomcat.embed.core;
    requires org.cryptotrader.security.library.services;

    exports org.cryptotrader.security.library.infrastructure;
}
