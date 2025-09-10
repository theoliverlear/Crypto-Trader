package org.cryptotrader.api.library.entity.user.admin;


import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.cryptotrader.api.library.entity.user.SafePassword;
import org.cryptotrader.api.library.entity.user.User;

@Entity
@Table(name = "admin_users")
@Getter
@Setter
public class AdminUser extends User {
    public AdminUser() {
        super();
    }

    public AdminUser(String username, String rawPassword) {
        super(username, rawPassword);
    }

    public AdminUser(String username, SafePassword encodedPassword) {
        super(username, encodedPassword);
    }
}


