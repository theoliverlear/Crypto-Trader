package org.theoliverlear.controller;

//=================================-Imports-==================================
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.theoliverlear.entity.user.User;

@RequestMapping("/api")
@RestController
public class CryptoTraderController {
    //============================-Variables-=================================
    private User currentUser;

}
