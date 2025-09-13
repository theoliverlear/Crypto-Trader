open module org.cryptotrader.api.library.communication {
    requires kotlin.stdlib;
    requires static lombok;
    requires static com.fasterxml.jackson.annotation;
    requires org.cryptotrader.api.library.models;
    
    exports org.cryptotrader.api.library.communication.request;
    exports org.cryptotrader.api.library.communication.request.alias;
    exports org.cryptotrader.api.library.communication.response;
}