package org.theoliverlear.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.theoliverlear.entity.User;
import org.theoliverlear.repository.UserRepository;

@Service
public class UserService {
    UserRepository userRepository;
    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    public boolean userExists(String username) {
        return this.userRepository.existsByUsername(username);
    }
    public User getUserByUsername(String username) {
        return this.userRepository.getUserByUsername(username);
    }
    public boolean comparePassword(User user, String password) {
        return user.getSafePassword().compareUnencodedPassword(password);
    }
    public void saveUser(User user) {
        this.userRepository.save(user);
    }
}
