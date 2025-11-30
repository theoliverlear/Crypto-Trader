package org.cryptotrader.data.library.communication.request;

import lombok.Data;

@Data
public class NewsSentimentHarvestRequest {
    private int numArticles;
    private int daysOffset;
    private int numDays;
    private boolean includeForbes;
    public NewsSentimentHarvestRequest(int numArticles,
                                       int daysOffset,
                                       int numDays,
                                       boolean includeForbes) {
        this.numArticles = numArticles;
        this.daysOffset = daysOffset;
        this.numDays = numDays;
        this.includeForbes = includeForbes;
    }
}
