package org.cryptotrader.repository;

import org.cryptotrader.entity.user.ProfilePicture;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfilePictureRepository extends JpaRepository<ProfilePicture, Long> {
    ProfilePicture findByUserId(Long userId);
    boolean existsByUserId(Long userId);
}
