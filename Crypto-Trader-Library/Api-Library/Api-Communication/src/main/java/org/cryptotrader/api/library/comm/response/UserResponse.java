package org.cryptotrader.api.library.comm.response;
//=================================-Imports-==================================
import lombok.Data;

@Data
public class UserResponse {
    //============================-Variables-=================================
    private String message;
    //===========================-Constructors-===============================
    public UserResponse(String message) {
        this.message = message;
    }
}
