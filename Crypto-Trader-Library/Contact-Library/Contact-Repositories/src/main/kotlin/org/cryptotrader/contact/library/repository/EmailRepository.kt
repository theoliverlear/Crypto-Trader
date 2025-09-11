package org.cryptotrader.contact.library.repository

import org.cryptotrader.contact.library.entity.Email
import org.springframework.data.jpa.repository.JpaRepository

interface EmailRepository : JpaRepository<Email, Long> {
    
}