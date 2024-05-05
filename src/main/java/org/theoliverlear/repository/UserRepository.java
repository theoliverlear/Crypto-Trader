package org.theoliverlear.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.theoliverlear.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User getUserByUsername(String username);
    User getUserById(Long id);
    boolean existsByUsername(String username);
}
