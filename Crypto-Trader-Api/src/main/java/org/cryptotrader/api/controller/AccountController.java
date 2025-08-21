package org.cryptotrader.api.controller;
//=================================-Imports-==================================
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.cryptotrader.comm.response.HasProfilePictureResponse;
import org.cryptotrader.comm.response.OperationSuccessfulResponse;
import org.cryptotrader.entity.user.ProductUser;
import org.cryptotrader.entity.user.ProfilePicture;
import org.cryptotrader.api.service.CryptoTraderService;
import org.cryptotrader.api.service.ProfilePictureService;
import org.cryptotrader.api.service.ProductUserService;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/api/account")
public class AccountController {
    //============================-Variables-=================================
    private CryptoTraderService cryptoTraderService;
    private ProfilePictureService profilePictureService;
    private ProductUserService productUserService;
    //===========================-Constructors-===============================
    @Autowired
    public AccountController(CryptoTraderService cryptoTraderService,
                             ProfilePictureService profilePictureService,
                             ProductUserService productUserService) {
        this.cryptoTraderService = cryptoTraderService;
        this.profilePictureService = profilePictureService;
        this.productUserService = productUserService;
    }
    //=============================-Methods-==================================

    //----------------------------Upload-Image--------------------------------
    @RequestMapping("/image/upload")
    public ResponseEntity<OperationSuccessfulResponse> uploadProfilePicture(@RequestParam("file") MultipartFile file,
                                                                            HttpSession session) {
        boolean userInSession = this.cryptoTraderService.userInSession(session);
        if (!userInSession) {
            return new ResponseEntity<>(new OperationSuccessfulResponse(false), HttpStatus.UNAUTHORIZED);
        }
        Optional<ProductUser> possibleSessionUser = this.cryptoTraderService.getUserFromSession(session);
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
    @RequestMapping("/get/{id}/has-profile-picture")
    public ResponseEntity<HasProfilePictureResponse> hasProfilePicture(@PathVariable String id) {
        boolean hasProfilePicture = this.profilePictureService.existsByUserId(Long.parseLong(id));
        return ResponseEntity.ok(new HasProfilePictureResponse(hasProfilePicture));
    }
    @Transactional
    @RequestMapping("/get/{id}/profile-picture")
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

