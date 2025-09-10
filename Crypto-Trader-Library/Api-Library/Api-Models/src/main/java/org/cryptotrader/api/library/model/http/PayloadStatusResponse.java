package org.cryptotrader.api.library.model.http;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class PayloadStatusResponse<T> {
    //=============================-Variables-=================================
    private T payload;
    private HttpStatus status;
    public PayloadStatusResponse(T payload, HttpStatus status) {
        this.payload = payload;
        this.status = status;
    }
}
