package org.theoliverlear.repository;
//=================================-Imports-==================================
import org.springframework.data.jpa.repository.JpaRepository;
import org.theoliverlear.entity.user.User;

public interface UserRepository extends JpaRepository<User, Long> {
    //============================-Methods-===================================

    //------------------------Get-User-By-Username----------------------------
    User getUserByUsername(String username);
    //---------------------------Get-User-By-Id-------------------------------
    User getUserById(Long id);
    //-------------------------Exists-By-Username-----------------------------
    boolean existsByUsername(String username);
}
