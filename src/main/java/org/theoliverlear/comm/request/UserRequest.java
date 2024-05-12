package org.theoliverlear.comm.request;

import lombok.Data;

@Data
public class UserRequest {
    private String username;
    private String password;
    //============================-Constructors-==============================
    public UserRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
