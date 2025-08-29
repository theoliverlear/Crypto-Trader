module org.cryptotrader.version.model {
    requires static lombok;
    requires org.jdom2;
    requires org.slf4j;
    requires java.xml;

    exports org.cryptotrader.version.model.config;
    exports org.cryptotrader.version.model.dependency;
    exports org.cryptotrader.version.model.dependency.type;
    exports org.cryptotrader.version.model.module;
    exports org.cryptotrader.version.model.module.type;
    exports org.cryptotrader.version.model.element;
}