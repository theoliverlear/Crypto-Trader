open module org.cryptotrader.console.library.communication {
    requires kotlin.stdlib;
    requires static lombok;
    requires static com.fasterxml.jackson.annotation;
    
    exports org.cryptotrader.console.library.communication.request;
    exports org.cryptotrader.console.library.communication.response;
}