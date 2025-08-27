package org.cryptotrader.promo;


import org.cryptotrader.promo.models.openai.*;
import org.cryptotrader.promo.models.openai.commit.CommitCapture;
import org.cryptotrader.promo.models.twitter.Tweeter;

public class CommitPromo {

    public static void main(String[] args) {
        CommitCapture capture = new CommitCapture();
        Summarizer summarizer = new Summarizer();
        String summary = summarizer.summarize(capture, PushPrompts.SYSTEM.getText(), PushPrompts.USER.getText());
        System.out.println(summary);

        String mode = System.getenv("RELEASEBOT_MODE");
        boolean shouldPost = mode != null && (mode.equalsIgnoreCase("prod") || mode.equalsIgnoreCase("post"));

        if (shouldPost) {
            Tweeter tweeter = new Tweeter();
            tweeter.tweet(summary);
        } else {
            System.out.println("[INFO] Dry run mode (RELEASEBOT_MODE=" + mode + "): not posting to X.");
        }
    }
}
