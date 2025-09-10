module org.cryptotrader.api.library {
    requires kotlin.stdlib;
    requires transitive org.cryptotrader.api.library.communication;
    requires transitive org.cryptotrader.api.library.components;
    requires transitive org.cryptotrader.api.library.models;
    requires transitive org.cryptotrader.api.library.repositories;
}