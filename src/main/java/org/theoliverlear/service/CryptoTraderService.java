package org.theoliverlear.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.theoliverlear.entity.User;
import org.theoliverlear.repository.UserRepository;

@Service
public class CryptoTraderService {
    UserRepository userRepository;
    @Autowired
    public CryptoTraderService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Transactional
    public void saveUser(User user) {
        this.userRepository.save(user);
    }

}
