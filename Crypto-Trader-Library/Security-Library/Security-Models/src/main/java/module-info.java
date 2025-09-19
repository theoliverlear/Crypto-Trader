open module org.cryptotrader.security.library.models {
    requires static jakarta.persistence;
    requires static lombok;
    requires kotlin.stdlib;
    requires transitive org.cryptotrader.api.library.models;

    exports org.cryptotrader.security.library.entity;
}
