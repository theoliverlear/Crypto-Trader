package org.cryptotrader.promo.models.openai;

import lombok.extern.slf4j.Slf4j;
import org.cryptotrader.promo.models.openai.commit.CommitCapture;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class Summarizer {
    private OpenAiChat chat;

    public Summarizer() {
        this.chat = new OpenAiChat();
    }

    public static String fromMessagesList(List<String> messages) {
        return "\nCommits:\n" + messages.stream().map(message -> {
            return "- " + message;
        }).collect(Collectors.joining("\n"));
    }
    
    public String summarize(@NotNull CommitCapture commits, String systemMessage, String userMessage) {
        List<String> messages = commits.getMessages();
        return this.summarize(messages, systemMessage, userMessage);
    }

    public String summarize(List<String> commits, String systemMessage, String userMessage) {
        String commitsText = fromMessagesList(commits);
        return this.chat.chat(systemMessage, userMessage + commitsText);
    }
    
    public String summarize(String summarizedContent, String systemMessage, String userMessage) {
        String response = this.chat.chat(systemMessage, userMessage + summarizedContent);
        if (response.length() > 280) {
            return this.summarize(summarizedContent, systemMessage, userMessage, 2);
        }
        return response;
    }

    public String summarize(String summarizedContent, String systemMessage, String userMessage, int numTries) {
        String response = this.chat.chat(systemMessage, userMessage + summarizedContent);
        log.info("Num tries for summary: {}", numTries);
        if (numTries >= 5) {
            throw new IllegalStateException("Failed to summarize content after 5 tries.");
        }
        if (response.length() > 280) {
            return this.summarize(summarizedContent, systemMessage, userMessage, numTries + 1);
        }
        return response;
    }
}
