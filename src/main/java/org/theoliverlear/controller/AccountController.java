package org.theoliverlear.controller;
//=================================-Imports-==================================
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.theoliverlear.entity.user.User;
import org.springframework.util.StringUtils;

@Controller
@RequestMapping("/account")
public class AccountController {
    //============================-Variables-=================================
    User currentUser;
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
    public ResponseEntity<String> uploadImage(@RequestParam("image") MultipartFile imageFile) {
        if (this.currentUser != null) {
            String imageFileName = StringUtils.cleanPath(imageFile.getOriginalFilename());
            return null;
        }
        return null;
    }
}
