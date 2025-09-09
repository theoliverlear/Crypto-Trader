package org.cryptotrader.repository

import org.cryptotrader.contact.entity.Email
import org.springframework.data.jpa.repository.JpaRepository

interface EmailRepository : JpaRepository<Email, Long> {
    
}