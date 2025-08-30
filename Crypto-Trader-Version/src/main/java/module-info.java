module org.cryptotrader.version {
    requires static lombok;
    requires org.jdom2;
    requires org.slf4j;
    requires java.xml;
    requires org.cryptotrader.version.model;
    requires org.apache.commons.io;
    requires org.apache.httpcomponents.httpcore;
    requires com.sigwarthsoftware.promo;
    requires com.sigwarthsoftware.openai;
    requires com.sigwarthsoftware.changelog;
    requires org.apache.httpcomponents.httpclient;


    exports org.cryptotrader.version;
    exports org.cryptotrader.version.script;
}