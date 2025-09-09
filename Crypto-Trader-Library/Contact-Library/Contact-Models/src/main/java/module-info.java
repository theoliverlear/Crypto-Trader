module org.cryptotrader.contact.models {
    requires kotlin.stdlib;
    requires jakarta.persistence;
    requires org.cryptotrader.api.models;
    requires static lombok;

    exports org.cryptotrader.contact.entity;
}