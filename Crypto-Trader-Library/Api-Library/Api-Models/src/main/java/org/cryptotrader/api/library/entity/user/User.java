package org.cryptotrader.api.library.entity.user;
//=================================-Imports-==================================

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.cryptotrader.universal.library.entity.Identifiable;

import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
public class User extends Identifiable {
    @Column(name = "username")
    protected String username;

    @Embedded
    @AttributeOverride(name = "encodedPassword", column = @Column(name = "password_hash"))
    protected SafePassword safePassword;

    @Column(name = "last_login")
    protected LocalDateTime lastLogin;

    public User() {
        super();
        this.username = null;
        this.safePassword = null;
        this.lastLogin = null;
    }

    public User(String username, String rawPassword) {
        super();
        this.username = username;
        this.safePassword = new SafePassword(rawPassword);
        this.lastLogin = LocalDateTime.now();
    }

    public User(String username, SafePassword encodedPassword) {
        super();
        this.username = username;
        this.safePassword = encodedPassword;
        this.lastLogin = LocalDateTime.now();
    }

    //=============================-Methods-==================================

    //-------------------------Update-Login-Time------------------------------
    public void updateLoginTime() {
        this.lastLogin = LocalDateTime.now();
    }
}
