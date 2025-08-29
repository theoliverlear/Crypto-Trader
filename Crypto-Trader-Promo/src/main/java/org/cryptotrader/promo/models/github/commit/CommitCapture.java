package org.cryptotrader.promo.models.github.commit;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class CommitCapture {
    private final CommitRange range;
    private final String githubToken = System.getenv("GITHUB_TOKEN");

    public CommitCapture() {
        this(CommitRange.fromSystem());
    }

    public CommitCapture(CommitRange commitRange) {
        this.range = commitRange;
    }

    /**
     * Calls the GitHub compare API and returns the raw JSON payload as a String.
     * If the provided range appears reversed (e.g., status="behind" with empty commits),
     * it retries once with swapped base/head and returns that response instead.
     */
    public String getApiString() {
        String start = this.range.startCommitSha();
        String end = this.range.endCommitSha();
        if (isInvalidRange(start, end)) {
            throw new IllegalStateException("START_COMMIT and END_COMMIT environment variables must be set");
        }
        HttpGet request = this.getHttpRequest();

        try (CloseableHttpClient client = HttpClients.custom().build();
             CloseableHttpResponse response = client.execute(request)) {
            int status = response.getStatusLine().getStatusCode();
            HttpEntity entity = response.getEntity();
            String body = entity != null ? EntityUtils.toString(entity, StandardCharsets.UTF_8) : "";
            if (status < 200 || status >= 300) {
                throw new IllegalStateException("GitHub compare API request failed (%d) for URL: %s body: %s".formatted(status, this.getUrlString(), body));
            }
            try {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode root = mapper.readTree(body);
                JsonNode commits = root.path("commits");
                boolean emptyCommits = !commits.isArray() || commits.isEmpty();
                String statusText = root.path("status").asText("");
                int aheadBy = root.path("ahead_by").asInt(0);
                int behindBy = root.path("behind_by").asInt(0);
                boolean looksReversed = isReversed(emptyCommits, statusText, aheadBy, behindBy);
                if (looksReversed) {
                    return this.getApiString(this.getInverseString());
                } else {
                    return body;
                }
            } catch (IOException _) { }
            return body;
        } catch (IOException exception) {
            throw new RuntimeException("Failed to call GitHub compare API: " + exception.getMessage(), exception);
        }
    }

    public String getApiString(String url) {
        String start = this.range.startCommitSha();
        String end = this.range.endCommitSha();
        if (isInvalidRange(start, end)) {
            throw new IllegalStateException("START_COMMIT and END_COMMIT environment variables must be set");
        }

        HttpGet request = this.getHttpRequest(url);
        try (CloseableHttpClient client = HttpClients.custom().build();
             CloseableHttpResponse response = client.execute(request)) {
            int status = response.getStatusLine().getStatusCode();
            HttpEntity entity = response.getEntity();
            String body = entity != null ? EntityUtils.toString(entity, StandardCharsets.UTF_8) : "";
            if (status < 200 || status >= 300) {
                throw new IllegalStateException("GitHub compare API request failed (%d) for URL: %s body: %s".formatted(status, url, body));
            }
            return body;
        } catch (IOException exception) {
            throw new RuntimeException("Failed to call GitHub compare API: " + exception.getMessage(), exception);
        }

    }

    private static boolean isReversed(boolean emptyCommits, String statusText, int aheadBy, int behindBy) {
        boolean looksReversed = emptyCommits && ("behind".equalsIgnoreCase(statusText) || (aheadBy == 0 && behindBy > 0));
        return looksReversed;
    }

    @NotNull
    private HttpGet getHttpRequest() {
        String url = this.getUrlString();
        HttpGet request = new HttpGet(url);
        request.addHeader("Accept", "application/vnd.github+json");
        request.addHeader("User-Agent", "Crypto-Trader-Promo/CommitCapture");
        if (isValidToken()) {
            request.addHeader("Authorization", "Bearer " + githubToken);
        }
        return request;
    }

    @NotNull
    private HttpGet getHttpRequest(String url) {
        HttpGet request = new HttpGet(url);
        request.addHeader("Accept", "application/vnd.github+json");
        request.addHeader("User-Agent", "Crypto-Trader-Promo/CommitCapture");
        if (isValidToken()) {
            request.addHeader("Authorization", "Bearer " + githubToken);
        }
        return request;
    }

    private boolean isValidToken() {
        return githubToken != null && !githubToken.isBlank();
    }

    private static boolean isInvalidRange(String start, String end) {
        return start == null || start.isBlank() || end == null || end.isBlank();
    }
    
    public List<String> getMessages() {
        String json = this.getApiString();
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode root = mapper.readTree(json);
            Set<String> messages = new LinkedHashSet<>();
            JsonNode commits = root.path("commits");
            if (commits.isArray()) {
                for (JsonNode commit : commits) {
                    String message = commit.path("commit").path("message").asText(null);
                    if (message != null && !message.isBlank()) {
                        messages.add(message);
                    }
                }
            }
            return new ArrayList<>(messages);
        } catch (IOException exception) {
            throw new RuntimeException("Failed to parse GitHub compare JSON: " + exception.getMessage(), exception);
        }
    }

    public String getUrlString() {
        final String baseUrl = "https://api.github.com/repos/theoliverlear/Crypto-Trader/compare/";
        return baseUrl + "%s...%s".formatted(this.range.startCommitSha(), this.range.endCommitSha());
    }
    
    public String getInverseString() {
        final String baseUrl = "https://api.github.com/repos/theoliverlear/Crypto-Trader/compare/";
        return baseUrl + "%s...%s".formatted(this.range.endCommitSha(), this.range.startCommitSha());
    }
}
