package org.cryptotrader.comm.response;

import lombok.Data;

import java.util.List;

@Data
public class TradeEventListResponse {
    private List<TradeEventResponse> events;
    public TradeEventListResponse(List<TradeEventResponse> events) {
        this.events = events;
    }
}
