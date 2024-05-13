package org.theoliverlear.service;
//=================================-Imports-==================================
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.theoliverlear.entity.user.User;
import org.theoliverlear.repository.UserRepository;

@Service
@Slf4j
public class UserService {
    //============================-Variables-=================================
    UserRepository userRepository;
    //===========================-Constructors-===============================
    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    //============================-Methods-===================================

    //----------------------User-Exists-By-Username---------------------------
    public boolean userExistsByUsername(String username) {
        return this.userRepository.existsByUsername(username);
    }
    //------------------------Get-User-By-Username----------------------------
    public User getUserByUsername(String username) {
        return this.userRepository.getUserByUsername(username);
    }
    //--------------------------Compare-Password------------------------------
    public boolean comparePassword(User user, String password) {
        boolean passwordsMatch = user.getSafePassword().compareUnencodedPassword(password);
        return passwordsMatch;
    }
    //-----------------------------Save-User----------------------------------
    public void saveUser(User user) {
        log.info("Saving user: {}", user.getUsername());
        this.userRepository.save(user);
    }
    //---------------------------Get-User-By-Id-------------------------------
    public User getUserById(Long id) {
        return this.userRepository.getUserById(id);
    }
}
