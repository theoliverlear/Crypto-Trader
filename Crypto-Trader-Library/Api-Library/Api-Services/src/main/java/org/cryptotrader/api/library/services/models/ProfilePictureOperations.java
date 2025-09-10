package org.cryptotrader.api.library.services.models;

import org.cryptotrader.api.library.entity.user.ProfilePicture;

import java.util.Optional;

/**
 * Operations for managing a user's profile picture. Extracted as an interface
 * so Spring can proxy via JDK dynamic proxies on newer JDKs without CGLIB.
 */
public interface ProfilePictureOperations {
    void saveProfilePicture(ProfilePicture profilePicture);
    Optional<ProfilePicture> findByUserId(Long userId);
    boolean existsByUserId(Long userId);
}
