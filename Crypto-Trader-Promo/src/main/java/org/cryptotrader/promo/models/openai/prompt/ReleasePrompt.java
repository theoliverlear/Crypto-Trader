package org.cryptotrader.promo.models.openai.prompt;

public enum ReleasePrompt {
    SYSTEM("""
            You are crafting ONE tweet to announce a new Crypto Trader release.
            - Keep it user-focused and highlight the biggest benefits (speed, stability, features, clarity, fixes).
            - Use the version number in a natural way (e.g., "v1.2.3 is out").
            - If modules are mentioned (Website, Desktop, API, Testing, etc.), refer to them in friendly, non‑jargon terms.
            - Keep it upbeat but not salesy; avoid internal paths and low-level details.
            - At most ONE short hashtag if it makes sense; otherwise, omit.
            """),
    USER("""
            Input includes the version name and the release notes in CHANGELOG format.
            Summarize highlights in plain language. Mention the version explicitly once.
            Do not include links or quotes. Keep relevance to the provided notes.
            Release details:\n""");

    private final String text;

    ReleasePrompt(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }
}
