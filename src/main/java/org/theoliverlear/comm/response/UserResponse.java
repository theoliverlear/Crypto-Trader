package org.theoliverlear.comm.response;

import lombok.Data;

@Data
public class UserResponse {
    String message;
    public UserResponse(String message) {
        this.message = message;
    }
}
