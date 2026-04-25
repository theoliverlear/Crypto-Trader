open module org.cryptotrader.logging.library.communication {
    requires kotlin.stdlib;
    requires static com.fasterxml.jackson.annotation;
    requires static com.fasterxml.jackson.core;
    requires static com.fasterxml.jackson.databind;

    exports org.cryptotrader.logging.library.communication.request;
    exports org.cryptotrader.logging.library.communication.response;
}
