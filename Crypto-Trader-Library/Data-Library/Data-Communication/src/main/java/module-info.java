module org.cryptotrader.data.library.communication {
    requires kotlin.stdlib;
    requires static lombok;
    requires static com.fasterxml.jackson.annotation;
    requires org.cryptotrader.data.library.models;
    
    exports org.cryptotrader.data.library.communication.request;
    exports org.cryptotrader.data.library.communication.response;
}