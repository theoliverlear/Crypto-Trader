open module org.cryptotrader.security.library.models {
    requires static jakarta.persistence;
    requires static lombok;
    requires org.cryptotrader.api.library.models;

    exports org.cryptotrader.security.library.entity;
}
