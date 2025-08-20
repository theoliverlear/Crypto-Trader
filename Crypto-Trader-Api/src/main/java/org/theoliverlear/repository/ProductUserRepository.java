package org.theoliverlear.repository;
//=================================-Imports-==================================
import org.springframework.data.jpa.repository.JpaRepository;
import org.theoliverlear.entity.user.ProductUser;
import org.theoliverlear.entity.user.User;

public interface ProductUserRepository extends JpaRepository<ProductUser, Long> {
    //============================-Methods-===================================

    //------------------------Get-User-By-Username----------------------------
    ProductUser getUserByUsername(String username);
    //---------------------------Get-User-By-Id-------------------------------
    ProductUser getUserById(Long id);
    //-------------------------Exists-By-Username-----------------------------
    boolean existsByUsername(String username);
}
