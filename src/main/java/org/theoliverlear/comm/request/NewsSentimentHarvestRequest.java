package org.theoliverlear.comm.request;

import lombok.Data;

@Data
public class NewsSentimentHarvestRequest {
    private int numArticles;
    private int daysOffset;
    private int numDays;
    public NewsSentimentHarvestRequest(int numArticles,
                                       int daysOffset,
                                       int numDays) {
        this.numArticles = numArticles;
        this.daysOffset = daysOffset;
        this.numDays = numDays;
    }
}
