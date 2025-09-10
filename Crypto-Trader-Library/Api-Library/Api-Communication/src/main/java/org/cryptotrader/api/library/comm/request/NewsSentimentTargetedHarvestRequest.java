package org.cryptotrader.api.library.comm.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class NewsSentimentTargetedHarvestRequest {
    private int numArticles;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean includeForbes;
    public NewsSentimentTargetedHarvestRequest(int numArticles,
                                               LocalDate startDate,
                                               LocalDate endDate,
                                               boolean includeForbes) {
        this.numArticles = numArticles;
        this.startDate = startDate;
        this.endDate = endDate;
        this.includeForbes = includeForbes;
    }
}
