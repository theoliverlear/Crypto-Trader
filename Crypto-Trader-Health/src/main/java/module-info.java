module org.cryptotrader.health {
    requires kotlin.stdlib;
    requires org.apache.httpcomponents.httpclient;
    requires org.apache.httpcomponents.httpcore;
    requires org.slf4j;
    
    exports org.cryptotrader.health;
    exports org.cryptotrader.health.models;
}