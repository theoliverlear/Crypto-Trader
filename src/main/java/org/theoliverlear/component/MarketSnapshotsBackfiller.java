package org.theoliverlear.component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

@Slf4j
@Component
@RequiredArgsConstructor
public class MarketSnapshotsBackfiller {
    private static final String BTC_CODE = "BTC";
    private static final String BTC_PRICE_COLUMN = "btc_price";
    private static final double WINDOW_SECONDS = 2.5;
    private static final int BATCH_SIZE = 10_000;
    private static final double NANO_TO_SECONDS = 1_000_000_000.0;
    private final JdbcTemplate jdbc;

    public void buildSnapshots(boolean fullRefresh) {
        List<String> codes = this.getCurrencyCodes();

        if (codes.isEmpty()) {
            log.warn("No non-BTC currencies found – nothing to do.");
            return;
        }
        long btcRows = this.getBtcRows();

        if (btcRows == 0) {
            log.warn("No BTC rows found – nothing to back-fill.");
            return;
        }
        this.initTable();

        this.jdbc.execute("""
        ALTER TABLE market_snapshots
        ADD COLUMN IF NOT EXISTS %s NUMERIC(34,18);
    """.formatted(BTC_PRICE_COLUMN));

        codes.forEach(code ->
                this.jdbc.execute("""
            ALTER TABLE market_snapshots
            ADD COLUMN IF NOT EXISTS %s NUMERIC(34,18);
        """.formatted(toPriceColumn(code)))
        );

        if (fullRefresh) {
            this.jdbc.execute("TRUNCATE TABLE market_snapshots;");
        }
        int cores = Math.min(Runtime.getRuntime().availableProcessors(), 18);
        ExecutorService pool = Executors.newFixedThreadPool(cores);
        ScheduledExecutorService ticker = Executors.newSingleThreadScheduledExecutor();

        LongAdder inserted = new LongAdder();
        long started = System.nanoTime();
        final long boot = started;

        runRowTracker(ticker, inserted, boot);
        this.runInsertThreads(btcRows, pool, codes, inserted);
        pool.shutdown();
        try {
            pool.awaitTermination(365, TimeUnit.DAYS);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
        ticker.shutdownNow();

        double sec = (System.nanoTime() - started) / NANO_TO_SECONDS;
        log.info("Snapshot back-fill complete – {} rows, {} rows/s ({} threads).",
                inserted.sum(),
                inserted.sum() / Math.max(sec, 0.001),
                cores);
    }

    private void initTable() {
        this.jdbc.execute("""
        CREATE TABLE IF NOT EXISTS market_snapshots(
            id           BIGSERIAL    PRIMARY KEY,
            last_updated TIMESTAMPTZ UNIQUE
        );
    """);
    }

    private Long getBtcRows() {
        return this.jdbc.queryForObject("""
                        SELECT COUNT(*) FROM currency_history
                        WHERE  currency_code = ?""",
                Long.class,
                BTC_CODE);
    }

    private List<String> getCurrencyCodes() {
        return this.jdbc.queryForList(
                "SELECT DISTINCT currency_code FROM currencies " +
                        "WHERE currency_code <> ? ORDER BY 1",
                String.class,
                BTC_CODE);
    }

    private void runInsertThreads(long btcRows, ExecutorService pool, List<String> codes, LongAdder inserted) {
        LongStream.range(0, (btcRows + BATCH_SIZE - 1) / BATCH_SIZE)
                .parallel()
                .forEach(slice -> pool.submit(() -> {
                    int offset = (int) (slice * BATCH_SIZE);
                    int rows = processSlice(offset, codes);
                    inserted.add(rows);
                }));
    }

    private static void runRowTracker(ScheduledExecutorService ticker, LongAdder inserted, long boot) {
        ticker.scheduleAtFixedRate(new Runnable() {
            long lastCount = 0;
            public void run() {
                long current = inserted.sum();
                long delta = current - lastCount;
                lastCount = current;
                double elapsedSec = (System.nanoTime() - boot) / NANO_TO_SECONDS;
                double avgRps = elapsedSec == 0 ? 0 : current / elapsedSec;
                log.info("{} rows/s   |   {} rows/s avg   |   {} total",
                        delta, Math.round(avgRps), current);

            }
        }, 1, 1, TimeUnit.SECONDS);
    }

    private int processSlice(int offset, List<String> codes) {
        String insertCols = "last_updated, " + BTC_PRICE_COLUMN + ", " +
                codes.stream().map(MarketSnapshotsBackfiller::toPriceColumn)
                        .collect(Collectors.joining(", "));

        String selectCols = "btc.currency_value AS " + BTC_PRICE_COLUMN + ",\n        " +
                codes.stream()
                        .map(c -> "COALESCE(" + c.toLowerCase() + ".currency_value, NULL) AS "
                                + toPriceColumn(c))
                        .collect(Collectors.joining(",\n        "));

        String lateralJoins = codes.stream()
                .map(code -> """
                        LEFT JOIN LATERAL (
                            SELECT currency_value
                            FROM   currency_history
                            WHERE  currency_code = '%s'
                              AND  last_updated BETWEEN btc.last_updated - INTERVAL '%s seconds'
                                                    AND btc.last_updated + INTERVAL '%s seconds'
                            ORDER  BY last_updated DESC
                            LIMIT  1
                        ) %s ON TRUE
                        """.formatted(code, WINDOW_SECONDS, WINDOW_SECONDS, code.toLowerCase()))
                .collect(Collectors.joining("\n"));

        String sql = """
        INSERT INTO market_snapshots (%s)
        WITH btc_data AS (
            SELECT last_updated,
                   currency_value
            FROM   currency_history
            WHERE  currency_code = '%s'
            ORDER  BY last_updated ASC
            LIMIT  %d
            OFFSET %d
        )
        SELECT
            btc.last_updated,
            %s
        FROM   btc_data btc
        %s
        ORDER BY btc.last_updated ASC
        ON CONFLICT (last_updated) DO NOTHING;
        """.formatted(insertCols, BTC_CODE, BATCH_SIZE, offset,
                selectCols, lateralJoins);

        return this.jdbc.update(sql);
    }
    
    private void removeCloseEntries() {
        this.jdbc.execute("""

BEGIN;

WITH ordered AS (
    SELECT id,
           last_updated,
           lag(last_updated) OVER (ORDER BY last_updated) AS prev_time
    FROM   market_snapshots
)
DELETE FROM market_snapshots ms
    USING  ordered o
WHERE  ms.id = o.id
  AND  o.prev_time IS NOT NULL
  AND  o.last_updated - o.prev_time < INTERVAL '3.5 seconds';

COMMIT;

BEGIN;

ALTER TABLE market_snapshots DROP CONSTRAINT market_snapshots_pkey;
ALTER TABLE market_snapshots ALTER COLUMN id DROP NOT NULL;

UPDATE market_snapshots SET id = NULL;

WITH ordered AS (
    SELECT ctid,
           row_number() OVER (ORDER BY last_updated) AS new_id
    FROM   market_snapshots
)
UPDATE market_snapshots m
SET    id = o.new_id
FROM   ordered o
WHERE  m.ctid = o.ctid;

ALTER TABLE market_snapshots ALTER COLUMN id SET NOT NULL;
ALTER TABLE market_snapshots ADD PRIMARY KEY (id);

SELECT setval('market_snapshots_id_seq',
              (SELECT max(id) FROM market_snapshots),
              true);

COMMIT;
"""
        );
    }

    private static String toPriceColumn(String code) {
        return code.toLowerCase() + "_price";
    }
}