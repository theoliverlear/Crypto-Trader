package org.cryptotrader.logging.library.repository;

import org.cryptotrader.logging.library.entity.FrontendLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface FrontendLogRepository extends JpaRepository<FrontendLog, Long> {

    List<FrontendLog> findByLevelAndTimestampBetween(
            String level, LocalDateTime from, LocalDateTime to);

    List<FrontendLog> findByContextAndTimestampAfter(
            String context, LocalDateTime after);
}
