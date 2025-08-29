package org.cryptotrader.promo.models.publish;

import lombok.extern.slf4j.Slf4j;
import org.cryptotrader.promo.models.twitter.Tweeter;

@Slf4j
public class Publisher {
    public static void publish(String message) {
        log.info(message);
        String mode = System.getenv("RELEASEBOT_MODE");
        boolean publishEnabled = mode.equalsIgnoreCase("prod") ||
                                 mode.equalsIgnoreCase("post");
        publish(message, publishEnabled);
    }
    public static void publish(String message, boolean shouldPublish) {
        if (shouldPublish) {
            Tweeter tweeter = new Tweeter();
            tweeter.tweet(message);
        } else {
            log.warn("[INFO] Dry run mode: not posting to X.");
        }
    }
}
