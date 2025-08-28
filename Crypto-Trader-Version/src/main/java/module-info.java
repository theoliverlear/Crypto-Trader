module org.cryptotrader.version {
    requires static lombok;
    requires org.jdom2;
    requires org.slf4j;
    requires java.xml;

    exports org.cryptotrader.version;
    exports org.cryptotrader.version.model;
    exports org.cryptotrader.version.model.config;
    exports org.cryptotrader.version.model.dependency;
    exports org.cryptotrader.version.script;
}