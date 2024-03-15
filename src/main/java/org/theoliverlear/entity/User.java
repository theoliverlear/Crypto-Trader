package org.theoliverlear.entity;
//=================================-Imports-==================================
import jakarta.persistence.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User {
    //============================-Variables-=================================
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "username")
    private String username;
    @Column(name = "password_hash")
    private String encodedPassword;
    @Column(name = "portfolio_id")
    private Long portfolioId;
    @Column(name = "last_login")
    private LocalDateTime lastLogin;
    //===========================-Constructors-===============================
    public User() {
        this.username = null;
        this.encodedPassword = null;
        this.portfolioId = null;
        this.lastLogin = null;
    }
    public User(String username, String rawPassword, Long portfolioId) {
        this.username = username;
        this.encodedPassword = this.encodePassword(rawPassword);
        this.portfolioId = portfolioId;
        this.lastLogin = LocalDateTime.now();
    }
    public User(String username, String rawPassword, Long portfolioId, LocalDateTime lastLogin) {
        this.username = username;
        this.encodedPassword = rawPassword;
        this.portfolioId = portfolioId;
        this.lastLogin = lastLogin;
    }
    //=============================-Methods-==================================

    //--------------------------Encode-Password-------------------------------
    public String encodePassword(String rawPassword) {
        return new BCryptPasswordEncoder().encode(rawPassword);
    }
    public void updateLoginTime() {
        this.lastLogin = LocalDateTime.now();
    }
    //============================-Overrides-=================================

    //------------------------------Equals------------------------------------

    //------------------------------Hash-Code---------------------------------

    //------------------------------To-String---------------------------------

    //=============================-Getters-==================================
    public Long getId() {
        return this.id;
    }
    public String getUsername() {
        return this.username;
    }
    public String getEncodedPassword() {
        return this.encodedPassword;
    }
    public Long getPortfolioId() {
        return this.portfolioId;
    }
    public LocalDateTime getLastLogin() {
        return this.lastLogin;
    }
    //=============================-Setters-==================================
    public void setId(Long id) {
        this.id = id;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public void setEncodedPassword(String encodedPassword) {
        this.encodedPassword = encodedPassword;
    }
    public void setPortfolioId(Long portfolioId) {
        this.portfolioId = portfolioId;
    }
    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }
}
