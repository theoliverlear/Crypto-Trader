package org.theoliverlear.controller;
//=================================-Imports-==================================
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.theoliverlear.comm.response.HasProfilePictureResponse;
import org.theoliverlear.comm.response.OperationSuccessfulResponse;
import org.theoliverlear.entity.user.ProfilePicture;
import org.theoliverlear.entity.user.User;
import org.springframework.util.StringUtils;
import org.theoliverlear.service.ProfilePictureService;
import org.theoliverlear.service.UserService;

import java.io.IOException;

@Controller
@RequestMapping("/account")
public class AccountController {
    //============================-Variables-=================================
    User currentUser;
    private ProfilePictureService profilePictureService;
    private UserService userService;
    //===========================-Constructors-===============================
    @Autowired
    public AccountController(ProfilePictureService profilePictureService, UserService userService) {
        this.profilePictureService = profilePictureService;
        this.userService = userService;
    }
    //=============================-Methods-==================================

    //------------------------------Account-----------------------------------
    @RequestMapping("/")
    public String account(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            this.currentUser = user;
            return "account";
        } else {
            return "redirect:/user/";
        }
    }
    //----------------------------Upload-Image--------------------------------
    @RequestMapping("/image/upload")
    public ResponseEntity<OperationSuccessfulResponse> uploadProfilePicture(@RequestParam("file") MultipartFile file, HttpSession session) {
        User sessionUser = (User) session.getAttribute("user");
        if (sessionUser == null) {
            return new ResponseEntity<>(new OperationSuccessfulResponse(false), HttpStatus.UNAUTHORIZED);
        }
        String fileName = file.getOriginalFilename();
        try {
            byte[] fileData = file.getBytes();
            ProfilePicture profilePicture = new ProfilePicture(fileName, fileData, sessionUser);
            sessionUser.setProfilePicture(profilePicture);
            profilePicture.setUser(sessionUser);
            this.profilePictureService.saveProfilePicture(profilePicture);
            this.userService.saveUser(sessionUser);
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
        ProfilePicture profilePicture = this.profilePictureService.findByUserId(Long.parseLong(id));
        if (profilePicture == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            byte[] imageData = profilePicture.getFileData();
            String fileType = profilePicture.getFileType();
            return ResponseEntity.ok().contentType(MediaType.parseMediaType(fileType)).body(imageData);
        }
    }
}
