open module org.cryptotrader.health.library.models {
    requires jakarta.persistence;
    requires static lombok;
    requires kotlin.stdlib;
    requires org.apache.httpcomponents.httpclient;
    requires org.apache.httpcomponents.httpcore;
    requires org.slf4j;
    requires org.hibernate.orm.core;
    requires org.cryptotrader.universal.library.models;

    exports org.cryptotrader.health.library.entity;
    exports org.cryptotrader.health.library.model;
}
