open module org.cryptotrader.security.library.services {
    requires kotlin.stdlib;
    requires org.slf4j;
    requires com.google.crypto.tink;
    requires inet.ipaddr;
    requires org.apache.tomcat.embed.core;
    requires spring.context;

    exports org.cryptotrader.security.library.service;
}
