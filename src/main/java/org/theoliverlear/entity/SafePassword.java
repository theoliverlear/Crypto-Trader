package org.theoliverlear.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Getter
@Setter
public class SafePassword {
    @Id
    String encodedPassword;
    @Transient
    @JsonIgnore
    BCryptPasswordEncoder encoder;
    public SafePassword() {
        this.encoder = new BCryptPasswordEncoder();
        this.encodedPassword = null;
    }
    public SafePassword(String unencodedPassword) {
        this.encoder = new BCryptPasswordEncoder();
        this.encodedPassword = this.encodePassword(unencodedPassword);
    }
    public String encodePassword(String unencodedPassword) {
        return this.encoder.encode(unencodedPassword);
    }
    public boolean compareUnencodedPassword(String unencodedPassword) {
        return this.encoder.matches(unencodedPassword, this.encodedPassword);
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj instanceof SafePassword comparedSafePassword) {
            return this.encodedPassword.equals(comparedSafePassword.encodedPassword);
        }
        return false;
    }
}
