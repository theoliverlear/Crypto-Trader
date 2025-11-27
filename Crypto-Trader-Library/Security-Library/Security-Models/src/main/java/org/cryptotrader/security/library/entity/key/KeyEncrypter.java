package org.cryptotrader.security.library.entity.key;

public interface KeyEncrypter {
    String encrypt(String key);
    String decrypt(String key);
}