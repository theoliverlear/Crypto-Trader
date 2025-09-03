open module org.cryptotrader.docs {
    requires spring.boot.autoconfigure;
    requires spring.context;
    requires spring.core;
    requires spring.webmvc;
    requires io.swagger.v3.oas.models;

    exports org.cryptotrader.docs.autoconfigure;
}