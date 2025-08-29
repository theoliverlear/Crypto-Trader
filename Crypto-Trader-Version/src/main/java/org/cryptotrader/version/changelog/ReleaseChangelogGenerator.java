package org.cryptotrader.version.changelog;

import org.apache.commons.io.FileUtils;
import org.cryptotrader.externals.openai.OpenAiChat;
import org.cryptotrader.promo.models.github.commit.CommitCapture;
import org.cryptotrader.promo.models.github.commit.CommitRange;
import org.cryptotrader.version.changelog.prompt.ChangelogPrompt;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;

public class ReleaseChangelogGenerator {
    public static String getChangelogTemplate() {
        final String path = "../CHANGELOG.md";
        try {
            String content = FileUtils.readFileToString(new File(path), StandardCharsets.UTF_8);
            return content;
        } catch (IOException exception) {
            throw new IllegalStateException("Error in reading changelog file.", exception);
        }
    }

    public static String getChangelog(CommitRange commitRange, String version) {
        String changelogFormat = getChangelogTemplate();
        String systemPrompt = ChangelogPrompt.SYSTEM.getText().formatted(changelogFormat);
        CommitCapture capture = new CommitCapture(commitRange);
        String userPrompt = ChangelogPrompt.USER.getText().formatted(version, LocalDate.now().toString());
        List<String> messages = capture.getMessages();
        userPrompt += String.join("\n", messages);
        OpenAiChat chat = new OpenAiChat();
        String response = chat.chat(systemPrompt, userPrompt);
        writeVersionedFile(response, version);
        return response;
    }
    
    private static void writeVersionedFile(String markdown, String version) {
        String fileName = "%s.md".formatted(version);
        try {
            Files.writeString(Paths.get(fileName), markdown, StandardCharsets.UTF_8);
        } catch (IOException ignored) {
            throw new IllegalStateException("Error in writing changelog file.", ignored);
        }
    }
    
    public static void main(String[] args) {
        CommitRange commitRange = new CommitRange("1a57b6f858b416c4658a418ebdeb5daefa3f4fe4",
                                                  "c30bc68d562e3d31c4952dc1af51341095db9986");
        System.out.println(getChangelog(commitRange, "v0.1.7"));
    }
}
