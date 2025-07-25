package org.theoliverlear.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.theoliverlear.entity.currency.Currency;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MarketSnapshotService {
    private final JdbcTemplate jdbcTemplate;

    @Transactional
    public void saveSnapshot(Map<String, Currency> currencies) {
        if (currencies == null || currencies.isEmpty()) {
            return;
        }
        List<String> columns = new ArrayList<>();
        List<Object> params = new ArrayList<>();
        columns.add("last_updated");
        params.add(LocalDateTime.now(ZoneId.of("America/Chicago")));

        currencies.forEach((code, currency) -> {
            String col = code.toLowerCase() + "_price";
            this.jdbcTemplate.execute("""
                ALTER TABLE market_snapshots
                ADD COLUMN IF NOT EXISTS %s NUMERIC(34,18)
            """.formatted(col));

            columns.add(col);
            params.add(currency.getValue());
        });

        String colList = String.join(", ", columns);
        String questionMarks = columns.stream().map(c -> "?").collect(Collectors.joining(", "));
        String sql = "INSERT INTO market_snapshots (" + colList + ") VALUES (" + questionMarks + ")";

        try {
            this.jdbcTemplate.update(sql, params.toArray());
            log.debug("Inserted market snapshot with {} currencies.", currencies.size());
        } catch (DataAccessException exception) {
            log.error("Failed to insert market snapshot", exception);
            throw exception;
        }
    }
}
