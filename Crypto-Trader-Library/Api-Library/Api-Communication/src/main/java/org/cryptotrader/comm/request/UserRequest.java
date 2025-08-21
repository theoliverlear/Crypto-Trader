package org.cryptotrader.comm.request;
//=================================-Imports-==================================
import lombok.Data;

@Data
public class UserRequest {
    //============================-Variables-=================================
    private String username;
    private String email;
    private String password;
    //============================-Constructors-==============================
    public UserRequest(String username,
                       String email,
                       String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }
}
