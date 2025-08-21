package org.cryptotrader.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.cryptotrader.entity.user.ProfilePicture;

public interface ProfilePictureRepository extends JpaRepository<ProfilePicture, Long> {
    ProfilePicture findByUserId(Long userId);
    boolean existsByUserId(Long userId);
}
