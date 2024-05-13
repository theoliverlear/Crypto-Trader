package org.theoliverlear.comm.request;
//=================================-Imports-==================================
import lombok.Data;

@Data
public class UserRequest {
    //============================-Variables-=================================
    private String username;
    private String password;
    //============================-Constructors-==============================
    public UserRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
