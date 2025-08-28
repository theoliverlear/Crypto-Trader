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
        if (publishEnabled) {
            Tweeter tweeter = new Tweeter();
            tweeter.tweet(message);
        } else {
            log.warn("[INFO] Dry run mode (RELEASEBOT_MODE={}): not posting to X.", mode);
        }
    }
}
