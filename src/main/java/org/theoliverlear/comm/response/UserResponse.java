package org.theoliverlear.comm.response;
//=================================-Imports-==================================
import lombok.Data;

@Data
public class UserResponse {
    //============================-Variables-=================================
    String message;
    //===========================-Constructors-===============================
    public UserResponse(String message) {
        this.message = message;
    }
}
