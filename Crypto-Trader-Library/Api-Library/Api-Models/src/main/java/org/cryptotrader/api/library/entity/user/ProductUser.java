package org.cryptotrader.api.library.entity.user;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.cryptotrader.api.library.entity.portfolio.Portfolio;
import org.cryptotrader.api.library.entity.user.builder.ProductUserBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "product_users")
@Getter
@Setter
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class ProductUser extends User implements UserDetails {
    //============================-Variables-=================================
    @Column(name = "email")
    private String email;
    @JsonManagedReference
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "portfolio_id", referencedColumnName = "id")
    private Portfolio portfolio;
    @OneToOne
    @JoinColumn(name = "profile_picture_id")
    private ProfilePicture profilePicture;
    //===========================-Constructors-===============================
    public ProductUser() {
        super();
        this.username = null;
        this.email = null;
        this.safePassword = null;
        this.portfolio = null;
        this.lastLogin = null;
    }
    public ProductUser(String username, String rawPassword) {
        super(username, rawPassword);
        this.email = null;
    }
    public ProductUser(String username, String rawPassword, String email) {
        super(username, rawPassword);
        this.email = email;
    }
    public ProductUser(String username, SafePassword encodedPassword) {
        super(username, encodedPassword);
        this.email = null;
        this.portfolio = new Portfolio(this);
    }
    public ProductUser(String username, String email, SafePassword encodedPassword) {
        super(username, encodedPassword);
        this.email = email;
        this.portfolio = new Portfolio(this);
    }
    public ProductUser(String username,
                String email,
                SafePassword encodedPassword,
                Portfolio portfolio,
                ProfilePicture profilePicture,
                LocalDateTime lastLogin) {
        super(username, encodedPassword);
        this.email = email;
        this.portfolio = portfolio;
        this.profilePicture = profilePicture;
        this.lastLogin = lastLogin;
    }
    public ProductUser(String username, String rawPassword, Portfolio portfolio) {
        super(username, rawPassword);
        this.email = null;
        this.portfolio = portfolio;
    }
    public ProductUser(String username, String rawPassword, Portfolio portfolio, LocalDateTime lastLogin) {
        super(username, rawPassword);
        this.email = null;
        this.portfolio = portfolio;
        this.lastLogin = lastLogin;
    }

    public static ProductUserBuilder builder() {
        return new ProductUserBuilder();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return this.safePassword.getEncodedPassword();
    }


    //============================-Overrides-=================================

    //------------------------------Equals------------------------------------

    //------------------------------Hash-Code---------------------------------

    //------------------------------To-String---------------------------------
}
