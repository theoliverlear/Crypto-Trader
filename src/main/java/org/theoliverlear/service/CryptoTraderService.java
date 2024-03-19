package org.theoliverlear.service;
//=================================-Imports-==================================
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.theoliverlear.entity.User;
import org.theoliverlear.repository.UserRepository;

import java.util.ArrayList;

@Service
public class CryptoTraderService {
    //============================-Variables-=================================
    UserRepository userRepository;
    PortfolioService portfolioService;
    //===========================-Constructors-===============================
    @Autowired
    public CryptoTraderService(UserRepository userRepository,
                               PortfolioService portfolioService) {
        this.userRepository = userRepository;
        this.portfolioService = portfolioService;
    }
    //=============================-Methods-==================================
    @Transactional
    public void saveUser(User user) {
        this.userRepository.save(user);
    }
    @Transactional
    public User getUser(String username) {
        return this.userRepository.getUserByUsername(username);
    }
    @Transactional
    public ArrayList<User> getUsers() {
        return (ArrayList<User>) this.userRepository.findAll();
    }
}
