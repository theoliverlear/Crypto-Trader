package org.cryptotrader.api.service;
//=================================-Imports-==================================
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.cryptotrader.entity.user.ProfilePicture;
import org.cryptotrader.api.repository.ProfilePictureRepository;

import java.util.Optional;

@Service
public class ProfilePictureService {
    //============================-Variables-=================================
    private ProfilePictureRepository profilePictureRepository;
    //===========================-Constructors-===============================
    @Autowired
    public ProfilePictureService(ProfilePictureRepository profilePictureRepository) {
        this.profilePictureRepository = profilePictureRepository;
    }
    //============================-Methods-===================================
    public void saveProfilePicture(ProfilePicture profilePicture) {
        this.profilePictureRepository.save(profilePicture);
    }
    @Transactional
    public Optional<ProfilePicture> findByUserId(Long userId) {
        ProfilePicture profilePicture = this.profilePictureRepository.findByUserId(userId);
        if (profilePicture == null) {
            return Optional.empty();
        } else {
            return Optional.of(profilePicture);
        }
    }
    public boolean existsByUserId(Long userId) {
        return this.profilePictureRepository.existsByUserId(userId);
    }
}
