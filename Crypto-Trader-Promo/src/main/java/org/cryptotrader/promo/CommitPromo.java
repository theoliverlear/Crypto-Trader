package org.cryptotrader.promo;


import org.cryptotrader.promo.models.openai.*;
import org.cryptotrader.promo.models.openai.commit.CommitCapture;
import org.cryptotrader.promo.models.openai.prompt.PushPrompts;
import org.cryptotrader.promo.models.publish.Publisher;

public class CommitPromo {

    public static void main(String[] args) {
        CommitCapture capture = new CommitCapture();
        Summarizer summarizer = new Summarizer();
        String summary = summarizer.summarize(capture, PushPrompts.SYSTEM.getText(), PushPrompts.USER.getText());
        Publisher.publish(summary);
    }
}
