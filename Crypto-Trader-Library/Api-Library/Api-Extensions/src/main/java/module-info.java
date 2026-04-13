module org.cryptotrader.api.library.extensions {
    requires spring.web;
    requires spring.security.core;
    requires org.apache.tomcat.embed.core;
    requires kotlin.stdlib;
    requires static lombok;

    exports org.cryptotrader.api.library.extension;
}