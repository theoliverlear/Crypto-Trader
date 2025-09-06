open module org.cryptotrader.api.communication {
    exports org.cryptotrader.comm.request;
    exports org.cryptotrader.comm.response;
    requires kotlin.stdlib;
    requires static lombok;
    requires static com.fasterxml.jackson.annotation;

}