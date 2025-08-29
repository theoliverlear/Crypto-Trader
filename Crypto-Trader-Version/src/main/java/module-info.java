module org.cryptotrader.version {
    requires static lombok;
    requires org.jdom2;
    requires org.slf4j;
    requires java.xml;
    requires org.cryptotrader.version.model;
    requires org.cryptotrader.externals.openai;
    requires org.apache.commons.io;
    requires org.cryptotrader.promo;

    exports org.cryptotrader.version;
    exports org.cryptotrader.version.script;
}