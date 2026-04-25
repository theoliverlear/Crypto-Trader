open module org.cryptotrader.logging.library.models {
    requires jakarta.persistence;
    requires org.hibernate.orm.core;
    requires static lombok;
    exports org.cryptotrader.logging.library.entity;
}
