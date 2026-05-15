module org.cryptotrader.simulator.library.communication {
    requires kotlin.stdlib;

    exports org.cryptotrader.simulator.library.communication.request;
    exports org.cryptotrader.simulator.library.communication.response;

    opens org.cryptotrader.simulator.library.communication.request;
    opens org.cryptotrader.simulator.library.communication.response;
}