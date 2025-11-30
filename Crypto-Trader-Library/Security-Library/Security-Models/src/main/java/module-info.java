open module org.cryptotrader.security.library.models {
    requires static jakarta.persistence;
    requires static lombok;
    requires kotlin.stdlib;
    requires transitive org.cryptotrader.api.library.models;
    requires org.cryptotrader.universal.library.models;

    exports org.cryptotrader.security.library.entity.ip;
    exports org.cryptotrader.security.library.entity.key;
}
