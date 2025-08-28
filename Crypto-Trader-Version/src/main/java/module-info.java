module org.cryptotrader.version {
    requires static lombok;
    requires org.jdom2;
    requires org.slf4j;
    requires java.xml;
    requires org.cryptotrader.version.model;

    exports org.cryptotrader.version;
    exports org.cryptotrader.version.script;
}