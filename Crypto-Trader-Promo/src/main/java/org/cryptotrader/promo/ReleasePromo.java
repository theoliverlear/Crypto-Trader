package org.cryptotrader.promo;

import org.cryptotrader.promo.models.openai.Summarizer;
import org.cryptotrader.promo.models.openai.prompt.ReleasePrompt;
import org.cryptotrader.promo.models.openai.release.Release;
import org.cryptotrader.promo.models.publish.Publisher;


public class ReleasePromo {
    public static void main(String[] args) {
        Release release = Release.fromSystem();
        Summarizer summarizer = new Summarizer();
        String summary = summarizer.summarize(release.toString(), ReleasePrompt.SYSTEM.getText(), ReleasePrompt.USER.getText());
        Publisher.publish(summary);
    }
}