package org.cryptotrader.promo.models.twitter;

public record TwitterEnvironment(String consumerKey,
                                 String consumerSecret,
                                 String accessToken,
                                 String accessTokenSecret) {
    public static TwitterEnvironment fromSystem() {
        return new TwitterEnvironment(
                System.getenv("X_API_KEY"),
                System.getenv("X_SECRET"),
                System.getenv("X_ACCESS_TOKEN"),
                System.getenv("X_ACCESS_SECRET"));
    }
}
