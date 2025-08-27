open module org.cryptotrader.promo {
    requires annotations;
    requires com.fasterxml.jackson.databind;
    requires static lombok;
    requires openai.java.client.okhttp;
    requires openai.java.core;
    requires org.apache.httpcomponents.httpclient;
    requires org.apache.httpcomponents.httpcore;
    requires scribejava.apis;
    requires scribejava.core;
    requires org.slf4j;

    exports org.cryptotrader.promo;
    exports org.cryptotrader.promo.models.openai;
    exports org.cryptotrader.promo.models.twitter;
    exports org.cryptotrader.promo.models.openai.commit;
    exports org.cryptotrader.promo.models.openai.issue;
}