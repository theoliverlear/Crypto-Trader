package org.cryptotrader.logging.http;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ansi.AnsiColor;
import org.springframework.boot.ansi.AnsiOutput;
import org.springframework.boot.ansi.AnsiStyle;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;

/**
 * Colorful, structured HTTP exchange logger for both HTTP and WebSocket handshakes.
 */
@Slf4j
public class HttpExchangeLoggingFilter extends OncePerRequestFilter {

    private final boolean includeQueryString;
    private final boolean includeRequestPayload;
    private final int maxRequestPayloadLength;
    private final boolean includeHeaders;
    private final boolean includeResponsePayload;
    private final int maxResponsePayloadLength;
    private final boolean colorEnabled;

    public HttpExchangeLoggingFilter(boolean includeQueryString,
                                     boolean includeRequestPayload,
                                     int maxRequestPayloadLength,
                                     boolean includeHeaders,
                                     boolean includeResponsePayload,
                                     int maxResponsePayloadLength,
                                     boolean colorEnabled) {
        this.includeQueryString = includeQueryString;
        this.includeRequestPayload = includeRequestPayload;
        this.maxRequestPayloadLength = maxRequestPayloadLength;
        this.includeHeaders = includeHeaders;
        this.includeResponsePayload = includeResponsePayload;
        this.maxResponsePayloadLength = maxResponsePayloadLength;
        this.colorEnabled = colorEnabled;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        ContentCachingRequestWrapper req = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper res = new ContentCachingResponseWrapper(response);
        long start = System.currentTimeMillis();
        Exception ex = null;
        try {
            filterChain.doFilter(req, res);
        } catch (Exception e) {
            ex = e;
            throw e;
        } finally {
            long tookMs = System.currentTimeMillis() - start;
            try {
                logExchange(req, res, tookMs, ex);
            } catch (Exception loggingEx) {
                log.warn("Failed to log HTTP exchange", loggingEx);
            }
            // Important: copy cached body back to response
            try {
                res.copyBodyToResponse();
            } catch (IOException ignore) {
                // ignore
            }
        }
    }

    private void logExchange(ContentCachingRequestWrapper req, ContentCachingResponseWrapper res, long tookMs, @Nullable Exception ex) {
        String scheme = req.getScheme();
        String protocol = req.getProtocol(); // e.g., HTTP/1.1
        String method = req.getMethod();
        String uri = req.getRequestURI();
        String query = req.getQueryString();
        String remote = req.getRemoteAddr();
        int status = res.getStatus();
        boolean isWebSocket = isWebSocketUpgrade(req);

        StringBuilder sb = new StringBuilder(256);
        // Prefix
        sb.append(color("[", AnsiColor.BRIGHT_BLACK)).append(color(isWebSocket ? "WS" : "HTTP", AnsiColor.CYAN, AnsiStyle.BOLD))
          .append(color("] ", AnsiColor.BRIGHT_BLACK));
        // Method / Status
        sb.append(color(method, AnsiColor.BLUE, AnsiStyle.BOLD)).append(' ');
        // Path
        sb.append(color(uri, AnsiColor.WHITE));
        if (includeQueryString && StringUtils.hasText(query)) {
            sb.append(color("?" + query, AnsiColor.BRIGHT_BLACK));
        }
        // Protocol
        sb.append(' ').append(color(protocol, AnsiColor.BRIGHT_BLACK));
        // Status
        sb.append(' ').append(color(Integer.toString(status), statusColor(status), AnsiStyle.BOLD));
        // Duration
        sb.append(' ').append(color(tookMs + "ms", durationColor(tookMs)));
        // Sizes
        int reqLen = req.getContentLength();
        int resLen = res.getContentSize();
        if (reqLen >= 0) {
            sb.append(' ').append(color(humanSize(reqLen) + "->", AnsiColor.MAGENTA));
        }
        if (resLen >= 0) {
            sb.append(color(humanSize(resLen), AnsiColor.MAGENTA));
        }
        // Origin
        sb.append(' ').append(color("from " + remote, AnsiColor.BRIGHT_BLACK));
        // Scheme
        sb.append(' ').append(color("over " + scheme, AnsiColor.BRIGHT_BLACK));

        if (includeHeaders) {
            sb.append('\n');
            appendHeaders(sb, "Request-Headers", req);
            appendHeaders(sb, "Response-Headers", res);
        }

        if (includeRequestPayload) {
            String payload = getBody(req.getContentAsByteArray(), req.getCharacterEncoding(), maxRequestPayloadLength);
            if (StringUtils.hasText(payload)) {
                sb.append('\n').append(color("Request-Payload:", AnsiColor.BRIGHT_BLACK)).append(' ')
                  .append(color(payload, AnsiColor.WHITE));
            }
        }
        if (includeResponsePayload) {
            String payload = getBody(res.getContentAsByteArray(), res.getCharacterEncoding(), maxResponsePayloadLength);
            if (StringUtils.hasText(payload)) {
                sb.append('\n').append(color("Response-Payload:", AnsiColor.BRIGHT_BLACK)).append(' ')
                  .append(color(payload, AnsiColor.WHITE));
            }
        }

        if (ex == null && status < 400) {
            log.info(sb.toString());
        } else if (status < 500) {
            log.warn(sb.toString(), ex);
        } else {
            log.error(sb.toString(), ex);
        }
    }

    private void appendHeaders(StringBuilder sb, String title, HttpServletRequest req) {
        sb.append(color(title + ":", AnsiColor.BRIGHT_BLACK)).append('\n');
        for (Enumeration<String> names = req.getHeaderNames(); names.hasMoreElements(); ) {
            String name = names.nextElement();
            List<String> values = java.util.Collections.list(req.getHeaders(name));
            sb.append("  ").append(color(name + ": ", AnsiColor.BRIGHT_BLACK)).append(color(String.join(", ", values), AnsiColor.WHITE)).append('\n');
        }
    }

    private void appendHeaders(StringBuilder sb, String title, HttpServletResponse res) {
        sb.append(color(title + ":", AnsiColor.BRIGHT_BLACK)).append('\n');
        for (String name : res.getHeaderNames()) {
            sb.append("  ").append(color(name + ": ", AnsiColor.BRIGHT_BLACK)).append(color(String.join(", ", res.getHeaders(name)), AnsiColor.WHITE)).append('\n');
        }
    }

    private boolean isWebSocketUpgrade(HttpServletRequest req) {
        String upgrade = req.getHeader(HttpHeaders.UPGRADE);
        return upgrade != null && "websocket".equalsIgnoreCase(upgrade);
    }

    private String humanSize(int bytes) {
        if (bytes < 1024) return bytes + "B";
        int kb = bytes / 1024;
        if (kb < 1024) return kb + "KB";
        int mb = kb / 1024;
        return mb + "MB";
    }

    private String getBody(byte[] buf, @Nullable String encoding, int max) {
        if (buf == null || buf.length == 0) return "";
        int len = Math.min(buf.length, max);
        Charset charset = Charset.forName(Objects.requireNonNullElse(encoding, Charset.defaultCharset().name()));
        String body = new String(buf, 0, len, charset);
        if (buf.length > max) {
            body += "…";
        }
        return body;
    }

    private AnsiColor statusColor(int status) {
        if (status >= 500) return AnsiColor.RED;
        if (status >= 400) return AnsiColor.YELLOW;
        if (status >= 300) return AnsiColor.CYAN;
        return AnsiColor.GREEN;
    }

    private AnsiColor durationColor(long ms) {
        if (ms > 2000) return AnsiColor.RED;
        if (ms > 700) return AnsiColor.YELLOW;
        return AnsiColor.GREEN;
    }

    private String color(String text, AnsiColor color) {
        return color(text, color, null);
    }

    private String color(String text, AnsiColor color, @Nullable AnsiStyle style) {
        if (!colorEnabled) return text;
        if (style != null) {
            return AnsiOutput.toString(style, color, text, AnsiStyle.NORMAL);
        }
        return AnsiOutput.toString(color, text, AnsiColor.DEFAULT);
    }
}