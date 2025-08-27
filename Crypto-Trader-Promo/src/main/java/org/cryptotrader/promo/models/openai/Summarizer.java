package org.cryptotrader.promo.models.openai;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

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
    
    public String summarize(String commits, String systemMessage, String userMessage) {
        return this.chat.chat(systemMessage, userMessage + commits);
    }
}
