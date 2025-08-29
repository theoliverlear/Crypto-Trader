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
    requires org.cryptotrader.externals.openai;

    exports org.cryptotrader.promo;
    exports org.cryptotrader.promo.models.openai;
    exports org.cryptotrader.promo.models.twitter;
    exports org.cryptotrader.promo.models.github.commit;
    exports org.cryptotrader.promo.models.github.issue;
    exports org.cryptotrader.promo.models.openai.prompt;
}