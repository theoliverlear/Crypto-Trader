package org.cryptotrader.promo.models.openai;

public record CommitRange(String startCommitSha, String endCommitSha) {
    public static CommitRange fromSystem() {
        return new CommitRange(
                System.getenv("START_COMMIT"),
                System.getenv("END_COMMIT")
        );
    }
}
