package org.theoliverlear.comm.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class NewsSentimentTargetedHarvestRequest {
    private int numArticles;
    private LocalDate startDate;
    private LocalDate endDate;
    public NewsSentimentTargetedHarvestRequest(int numArticles,
                                               LocalDate startDate,
                                               LocalDate endDate) {
        this.numArticles = numArticles;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
