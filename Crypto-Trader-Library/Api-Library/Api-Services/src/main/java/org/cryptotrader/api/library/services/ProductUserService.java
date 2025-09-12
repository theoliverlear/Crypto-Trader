package org.cryptotrader.api.library.services;
//=================================-Imports-==================================

import lombok.extern.slf4j.Slf4j;
import org.cryptotrader.api.library.entity.user.ProductUser;
import org.cryptotrader.api.library.entity.user.User;
import org.cryptotrader.api.library.repository.ProductUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ProductUserService {
    //============================-Variables-=================================
    private ProductUserRepository productUserRepository;
    //===========================-Constructors-===============================
    @Autowired
    public ProductUserService(ProductUserRepository productUserRepository) {
        this.productUserRepository = productUserRepository;
    }
    //============================-Methods-===================================

    //----------------------User-Exists-By-Username---------------------------
    public boolean userExistsByUsername(String username) {
        return this.productUserRepository.existsByUsername(username);
    }
    
    public ProductUser getUserByEmail(String email) {
        return this.productUserRepository.getUserByEmail(email);
    }
    
    public boolean userExistsByEmail(String email) {
        return this.productUserRepository.existsByEmail(email);
    }
    //------------------------Get-User-By-Username----------------------------
    public ProductUser getUserByUsername(String username) {
        return this.productUserRepository.getUserByUsername(username);
    }
    //--------------------------Compare-Password------------------------------
    public boolean comparePassword(User user, String password) {
        boolean passwordsMatch = user.getSafePassword().compareUnencodedPassword(password);
        return passwordsMatch;
    }
    //-----------------------------Save-User----------------------------------
    public void saveUser(ProductUser user) {
        log.info("Saving user: {}", user.getUsername());
        this.productUserRepository.save(user);
    }
    //---------------------------Get-User-By-Id-------------------------------
    public User getUserById(Long id) {
        return this.productUserRepository.getUserById(id);
    }
}
