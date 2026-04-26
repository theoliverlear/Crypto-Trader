package org.cryptotrader.health.library.repository;

import org.cryptotrader.health.library.entity.HealthStatus;
import org.cryptotrader.health.library.model.CryptoTraderService;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface HealthStatusRepository extends JpaRepository<HealthStatus, Long> {

    List<HealthStatus> findByServiceOrderByCheckedAtDesc(
        CryptoTraderService service);

    List<HealthStatus> findByServiceAndCheckedAtBetween(
        CryptoTraderService service, LocalDateTime from, LocalDateTime to);

    HealthStatus findFirstByServiceOrderByCheckedAtDesc(
        CryptoTraderService service);
}
