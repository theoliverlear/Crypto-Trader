package org.cryptotrader.api.controller;
//=================================-Imports-==================================
import jakarta.servlet.http.HttpSession;
import org.cryptotrader.api.services.ProductUserService;
import org.cryptotrader.api.services.ProfilePictureService;
import org.cryptotrader.api.services.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.cryptotrader.comm.response.HasProfilePictureResponse;
import org.cryptotrader.comm.response.OperationSuccessfulResponse;
import org.cryptotrader.entity.user.ProductUser;
import org.cryptotrader.entity.user.ProfilePicture;

import java.io.IOException;
import java.util.Optional;

/**
 * The AccountController class provides RESTful endpoints for managing user
 * accounts, including functionality for uploading and retrieving profile
 * pictures, and checking if a user has a profile picture.
 *
 * @see org.cryptotrader.api.services.ProfilePictureService
 * @see org.cryptotrader.api.services.ProductUserService
 * @see org.cryptotrader.api.services.SessionService
 * @see org.cryptotrader.entity.user.ProfilePicture
 * @see org.cryptotrader.entity.user.ProductUser
 * @author theoliverlear - Oliver Lear Sigwarth
 */
@RestController
@RequestMapping("/api/account")
public class AccountController {
    //============================-Variables-=================================
    private SessionService sessionService;
    private ProfilePictureService profilePictureService;
    private ProductUserService productUserService;
    //===========================-Constructors-===============================
    @Autowired
    public AccountController(SessionService sessionService,
                             ProfilePictureService profilePictureService,
                             ProductUserService productUserService) {
        this.sessionService = sessionService;
        this.profilePictureService = profilePictureService;
        this.productUserService = productUserService;
    }
    //=============================-Methods-==================================

    //----------------------------Upload-Image--------------------------------
    @PostMapping("/image/upload")
    public ResponseEntity<OperationSuccessfulResponse> uploadProfilePicture(@RequestParam("file") MultipartFile file,
                                                                            HttpSession session) {
        boolean userInSession = this.sessionService.userInSession(session);
        if (!userInSession) {
            return new ResponseEntity<>(new OperationSuccessfulResponse(false), HttpStatus.UNAUTHORIZED);
        }
        Optional<ProductUser> possibleSessionUser = this.sessionService.getUserFromSession(session);
        if (possibleSessionUser.isEmpty()) {
            return new ResponseEntity<>(new OperationSuccessfulResponse(false), HttpStatus.UNAUTHORIZED);
        }
        ProductUser sessionUser = possibleSessionUser.get();
        String fileName = file.getOriginalFilename();
        try {
            byte[] fileData = file.getBytes();
            ProfilePicture profilePicture = ProfilePicture.builder()
                                                          .fileName(fileName)
                                                          .fileData(fileData)
                                                          .user(sessionUser)
                                                          .build();
            sessionUser.setProfilePicture(profilePicture);
            profilePicture.setUser(sessionUser);
            this.profilePictureService.saveProfilePicture(profilePicture);
            this.productUserService.saveUser(sessionUser);
            return new ResponseEntity<>(new OperationSuccessfulResponse(true), HttpStatus.OK);
        } catch (IOException ex) {
            return new ResponseEntity<>(new OperationSuccessfulResponse(false), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/get/{id}/has-profile-picture")
    public ResponseEntity<HasProfilePictureResponse> hasProfilePicture(@PathVariable String id) {
        boolean hasProfilePicture = this.profilePictureService.existsByUserId(Long.parseLong(id));
        return ResponseEntity.ok(new HasProfilePictureResponse(hasProfilePicture));
    }
    
    
    /**
     * Retrieves the profile picture for a user given their ID.
     * If no profile picture is found for the user, a 404 (NOT_FOUND) response
     * is returned.
     * If a profile picture exists, the image data is returned with the
     * appropriate content type.
     *
     * @param id the ID of the user whose profile picture is to be retrieved
     * @return a ResponseEntity containing the profile picture data as a byte
     *         array with the appropriate HTTP content type if available, or a
     *         404 NOT_FOUND response if no profile picture is found
     *
     * @see org.cryptotrader.entity.user.ProfilePicture
     * @see org.springframework.http.ResponseEntity
     *
     * @author theoliverlear - Oliver Lear Sigwarth
     */
    @Transactional
    @GetMapping("/get/{id}/profile-picture")
    public ResponseEntity<byte[]> getProfilePicture(@PathVariable String id) {
        long userId = Long.parseLong(id);
        Optional<ProfilePicture> possibleProfilePicture = this.profilePictureService.findByUserId(userId);
        if (possibleProfilePicture.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            ProfilePicture profilePicture = possibleProfilePicture.get();
            byte[] imageData = profilePicture.getFileData();
            String fileType = profilePicture.getFileType();
            return ResponseEntity.ok().contentType(MediaType.parseMediaType(fileType)).body(imageData);
        }
    }
}

