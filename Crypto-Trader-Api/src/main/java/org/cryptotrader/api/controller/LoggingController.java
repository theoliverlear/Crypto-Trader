package org.cryptotrader.api.controller;
//=================================-Imports-==================================
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.cryptotrader.logging.library.communication.request.FrontendLogErrorRequest;
import org.cryptotrader.logging.library.communication.request.FrontendLogRequest;
import org.cryptotrader.logging.library.communication.response.FrontendLogResponse;
import org.cryptotrader.logging.library.events.FrontendLogBatchEvent;
import org.cryptotrader.logging.library.events.FrontendLogEvent;
import org.cryptotrader.logging.library.events.LogEventBinding;
import org.cryptotrader.logging.library.events.publisher.LogEventsPublisher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.StringReader;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/logs")
@Slf4j
public class LoggingController {
    //============================-Variables-=================================
    private final LogEventsPublisher logEventsPublisher;
    private final ObjectMapper objectMapper;
    //===========================-Constructors-===============================
    @Autowired
    public LoggingController(LogEventsPublisher logEventsPublisher, ObjectMapper objectMapper) {
        this.logEventsPublisher = logEventsPublisher;
        this.objectMapper = objectMapper;
    }
    //=============================-Methods-==================================

    @PostMapping(value = "/website", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FrontendLogResponse> receiveSingleLog(
        @RequestBody FrontendLogRequest logEntry,
        HttpServletRequest request) {
        FrontendLogEvent event = this.mapToEvent(logEntry, request);
        FrontendLogBatchEvent batch = new FrontendLogBatchEvent(
            List.of(event), Instant.now());
        this.logEventsPublisher.publishBatch(LogEventBinding.FRONTEND_LOGS.getBindingName(), batch);
        return ResponseEntity.accepted()
            .body(new FrontendLogResponse(1, "accepted"));
    }

    @PostMapping(value = "/website", consumes = "application/x-ndjson")
    public ResponseEntity<FrontendLogResponse> receiveBatchLogs(
        @RequestBody String ndjsonBody,
        HttpServletRequest request) {
        List<FrontendLogEvent> entries = this.parseNdjson(ndjsonBody, request);
        FrontendLogBatchEvent batch = new FrontendLogBatchEvent(
            entries, Instant.now());
        this.logEventsPublisher.publishBatch(LogEventBinding.FRONTEND_LOGS.getBindingName(), batch);
        return ResponseEntity.accepted()
            .body(new FrontendLogResponse(entries.size(), "accepted"));
    }

    // TODO: Add to service class.
    private FrontendLogEvent mapToEvent(FrontendLogRequest dto, HttpServletRequest request) {
        FrontendLogErrorRequest error = dto.getError();
        return new FrontendLogEvent(
            dto.getTimestamp(),
            dto.getLevel(),
            dto.getLogger(),
            dto.getContext(),
            dto.getMessage(),
            dto.getMetadata(),
            error != null ? error.getName() : null,
            error != null ? error.getMessage() : null,
            error != null ? error.getStack() : null,
            request.getHeader("x-client-app"),
            request.getHeader("User-Agent"),
            resolveIpAddress(request),
            request.getRemoteAddr()
        );
    }

    // TODO: Add to service class.
    private String resolveIpAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isBlank()) {
            return xForwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }

    // TODO: Add to service class.
    private List<FrontendLogEvent> parseNdjson(String ndjsonBody, HttpServletRequest request) {
        List<FrontendLogEvent> events = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new StringReader(ndjsonBody))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.isBlank()) {
                    FrontendLogRequest dto = this.objectMapper.readValue(line, FrontendLogRequest.class);
                    events.add(mapToEvent(dto, request));
                }
            }
        } catch (Exception e) {
            log.error("Failed to parse NDJSON frontend log batch", e);
        }
        return events;
    }
}
