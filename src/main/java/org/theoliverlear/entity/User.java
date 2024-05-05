package org.theoliverlear.entity;
//=================================-Imports-==================================
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.theoliverlear.convert.entity.SafePasswordConverter;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {
    //============================-Variables-=================================
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "username")
    private String username;
    @Column(name = "password_hash")
    @Convert(converter = SafePasswordConverter.class)
    private SafePassword safePassword;
    @Column(name = "portfolio_id")
    private Long portfolioId;
    @Column(name = "last_login")
    private LocalDateTime lastLogin;
    //===========================-Constructors-===============================
    public User() {
        this.username = null;
        this.safePassword = null;
        this.portfolioId = null;
        this.lastLogin = null;
    }
    public User(String username, String rawPassword, Long portfolioId) {
        this.username = username;
        this.safePassword = new SafePassword(rawPassword);
        this.portfolioId = portfolioId;
        this.lastLogin = LocalDateTime.now();
    }
    public User(String username, String rawPassword, Long portfolioId, LocalDateTime lastLogin) {
        this.username = username;
        this.safePassword = new SafePassword(rawPassword);
        this.portfolioId = portfolioId;
        this.lastLogin = lastLogin;
    }
    //=============================-Methods-==================================


    public void updateLoginTime() {
        this.lastLogin = LocalDateTime.now();
    }
    //============================-Overrides-=================================

    //------------------------------Equals------------------------------------

    //------------------------------Hash-Code---------------------------------

    //------------------------------To-String---------------------------------


}
