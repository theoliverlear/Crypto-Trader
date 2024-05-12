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
        boolean passwordsMatch = user.getSafePassword().compareUnencodedPassword(password);
        return passwordsMatch;
    }
    public void saveUser(User user) {
        System.out.println("Saving user: " + user.getUsername());
        this.userRepository.save(user);
    }
    public User getUserById(Long id) {
        return this.userRepository.getUserById(id);
    }
}
