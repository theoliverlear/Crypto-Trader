package org.cryptotrader.api.library.repository;

import org.cryptotrader.api.library.entity.user.ProfilePicture;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfilePictureRepository extends JpaRepository<ProfilePicture, Long> {
    ProfilePicture findByUserId(Long userId);
    boolean existsByUserId(Long userId);
}
