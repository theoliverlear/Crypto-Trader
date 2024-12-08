package org.theoliverlear.entity.user;
//=================================-Imports-==================================
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.theoliverlear.convert.entity.SafePasswordConverter;
import org.theoliverlear.entity.portfolio.Portfolio;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class User {
    //============================-Variables-=================================
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "username")
    private String username;
    @Column(name = "email")
    private String email;
    @Column(name = "password_hash")
    @Convert(converter = SafePasswordConverter.class)
    private SafePassword safePassword;
    @JsonManagedReference
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "portfolio_id", referencedColumnName = "id")
    private Portfolio portfolio;
    @OneToOne
    @JoinColumn(name = "profile_picture_id")
    private ProfilePicture profilePicture;
    @Column(name = "last_login")
    private LocalDateTime lastLogin;
    //===========================-Constructors-===============================
    public User() {
        this.username = null;
        this.email = null;
        this.safePassword = null;
        this.portfolio = null;
        this.lastLogin = null;
    }
    public User(String username, String rawPassword) {
        this.username = username;
        this.email = null;
        this.safePassword = new SafePassword(rawPassword);
        this.lastLogin = LocalDateTime.now();
    }
    public User(String username, String rawPassword, String email) {
        this.username = username;
        this.email = email;
        this.safePassword = new SafePassword(rawPassword);
        this.lastLogin = LocalDateTime.now();
    }
    public User(String username, SafePassword encodedPassword) {
        this.username = username;
        this.email = null;
        this.safePassword = encodedPassword;
        this.portfolio = new Portfolio(this);
        this.lastLogin = LocalDateTime.now();
    }
    public User(String username, String email, SafePassword encodedPassword) {
        this.username = username;
        this.email = email;
        this.safePassword = encodedPassword;
        this.portfolio = new Portfolio(this);
        this.lastLogin = LocalDateTime.now();
    }
    public User(String username, String rawPassword, Portfolio portfolio) {
        this.username = username;
        this.email = null;
        this.safePassword = new SafePassword(rawPassword);
        this.portfolio = portfolio;
        this.lastLogin = LocalDateTime.now();
    }
    public User(String username, String rawPassword, Portfolio portfolio, LocalDateTime lastLogin) {
        this.username = username;
        this.email = null;
        this.safePassword = new SafePassword(rawPassword);
        this.portfolio = portfolio;
        this.lastLogin = lastLogin;
    }
    //=============================-Methods-==================================

    //-------------------------Update-Login-Time------------------------------
    public void updateLoginTime() {
        this.lastLogin = LocalDateTime.now();
    }
    //============================-Overrides-=================================

    //------------------------------Equals------------------------------------

    //------------------------------Hash-Code---------------------------------

    //------------------------------To-String---------------------------------


}
