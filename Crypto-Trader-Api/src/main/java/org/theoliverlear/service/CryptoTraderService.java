package org.theoliverlear.service;
//=================================-Imports-==================================
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import org.theoliverlear.entity.user.User;

import java.util.Optional;

@Service
public class CryptoTraderService {
    //============================-Variables-=================================

    //===========================-Constructors-===============================

    //=============================-Methods-==================================

    //--------------------------User-In-Session-------------------------------
    public boolean userInSession(HttpSession session) {
        return session.getAttribute("user") != null;
    }
    //-----------------------Get-User-From-Session----------------------------
    public Optional<User> getUserFromSession(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return Optional.empty();
        } else {
            return Optional.of(user);
        }
    }
    public void setSessionUser(HttpSession session, User user) {
        session.setAttribute("user", user);
    }
    public void removeSessionUser(HttpSession session) {
        session.removeAttribute("user");
    }
}