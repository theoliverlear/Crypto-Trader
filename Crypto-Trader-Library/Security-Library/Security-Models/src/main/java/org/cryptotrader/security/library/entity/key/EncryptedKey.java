package org.cryptotrader.security.library.entity.key;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.cryptotrader.universal.library.entity.Identifiable;
import org.cryptotrader.api.library.entity.user.ProductUser;

// Encrypts API keys
@Entity
@Table(name = "encrypted_keys")
@Getter
@Setter
@NoArgsConstructor
public class EncryptedKey extends Identifiable implements KeyEncrypter {
    
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private ProductUser user;
    @Column(name = "api_key", nullable = false)
    private String apiKey;
    @Enumerated(EnumType.STRING)
    @Column(name = "key_type", nullable = false)
    private KeyType keyType;

    public EncryptedKey(ProductUser user, KeyType keyType) {
        this.user = user;
        this.apiKey = null;
        this.keyType = keyType;
    }

    public EncryptedKey(ProductUser user, String apiKey, KeyType keyType) {
        this.user = user;
        this.apiKey = apiKey;
        this.keyType = keyType;
    }
    
    public static void setEncrypterDelegate(KeyEncrypter encrypterDelegate) {
        EncryptedKey.encrypterDelegate = encrypterDelegate;
    }
    
    @Transient
    private static KeyEncrypter encrypterDelegate;
    
    @Override
    public String encrypt(String key) {
        if (encrypterDelegate == null) {
            throw new IllegalStateException("Encrypter delegate not set in EncryptedKey class.");
        }
        return encrypterDelegate.encrypt(key);
    }

    @Override
    public String decrypt(String key) {
        if (encrypterDelegate == null) {
            throw new IllegalStateException("Encrypter delegate not set in EncryptedKey class.");
        }
        return encrypterDelegate.decrypt(key);
    }
}
