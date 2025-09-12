package org.cryptotrader.api.library.repository;
//=================================-Imports-==================================

import org.cryptotrader.api.library.entity.user.ProductUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductUserRepository extends JpaRepository<ProductUser, Long> {
    //============================-Methods-===================================

    //------------------------Get-User-By-Username----------------------------
    ProductUser getUserByUsername(String username);
    
    ProductUser getUserByEmail(String email);
    //---------------------------Get-User-By-Id-------------------------------
    ProductUser getUserById(Long id);
    //-------------------------Exists-By-Username-----------------------------
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
