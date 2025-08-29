package org.cryptotrader.promo.models.github.issue;

public record Issue(String title, String body, String tags) {
    public static Issue fromSystem() {
        return new Issue(
                System.getenv("ISSUE_TITLE"),
                System.getenv("ISSUE_BODY"),
                System.getenv("ISSUE_TAGS")
        );
    }
}
