package org.theoliverlear.comm.request;

public class UserRequest {
    private String username;
    private String password;
    //============================-Constructors-==============================
    public UserRequest() {
        this.username = "";
        this.password = "";
    }
    public UserRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
    //=============================-Getters-==================================
    public String getUsername() {
        return this.username;
    }
    public String getPassword() {
        return this.password;
    }
    //=============================-Setters-==================================
    public void setUsername(String username) {
        this.username = username;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}
