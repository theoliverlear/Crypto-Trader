open module org.cryptotrader.api.library.communication {
    exports org.cryptotrader.api.library.comm.request;
    exports org.cryptotrader.api.library.comm.response;
    requires kotlin.stdlib;
    requires static lombok;
    requires static com.fasterxml.jackson.annotation;

}