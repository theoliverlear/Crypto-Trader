package org.cryptotrader.api.library.entity.user;
//=================================-Imports-==================================
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Getter
@Setter
@Embeddable
public class SafePassword {
    //============================-Variables-=================================
    private String encodedPassword;
    @Transient
    @JsonIgnore
    private BCryptPasswordEncoder encoder;
    //===========================-Constructors-===============================
    public SafePassword() {
        this.encoder = new BCryptPasswordEncoder();
        this.encodedPassword = null;
    }
    public SafePassword(String unencodedPassword) {
        this.encoder = new BCryptPasswordEncoder();
        this.encodedPassword = this.encodePassword(unencodedPassword);
    }
    //============================-Methods-===================================

    //--------------------------Encode-Password-------------------------------
    public String encodePassword(String unencodedPassword) {
        String encoded = this.encoder.encode(unencodedPassword);
        this.encodedPassword = encoded;
        return encoded;
    }
    //---------------------Compare-Unencoded-Password-------------------------
    public boolean compareUnencodedPassword(String unencodedPassword) {
        if (this.encodedPassword == null || this.encodedPassword.isBlank()) {
            return false;
        }
        return this.encoder.matches(unencodedPassword, this.encodedPassword);
    }
    //============================-Overrides-=================================

    //------------------------------Equals------------------------------------
    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object instanceof SafePassword comparedSafePassword) {
            return this.encodedPassword != null && this.encodedPassword.equals(comparedSafePassword.encodedPassword);
        }
        return false;
    }
}
