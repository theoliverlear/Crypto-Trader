package org.cryptotrader.repository;
//=================================-Imports-==================================

import org.cryptotrader.entity.user.ProductUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductUserRepository extends JpaRepository<ProductUser, Long> {
    //============================-Methods-===================================

    //------------------------Get-User-By-Username----------------------------
    ProductUser getUserByUsername(String username);
    //---------------------------Get-User-By-Id-------------------------------
    ProductUser getUserById(Long id);
    //-------------------------Exists-By-Username-----------------------------
    boolean existsByUsername(String username);
}
