package org.cryptotrader.promo.models.openai;

public enum PushPrompts {
    SYSTEM("""
            You are ReleaseBot, a concise release promo copywriter. You are providing updates about the app Crypto Trader.
                    Given a Git diff summary (file changes and commit messages), write ONE tweet
                    that highlights user-visible improvements (not filenames), avoids jargon, and reads naturally.
                    Keep it upbeat but not cringey. No emojis unless clearly appropriate.
                    Return only the tweet text, no quotes. Max chars at 280."""),
    USER("""
            Focus on what a user gets (speed, reliability, new feature, bug fix). Find the tangible benefit and promote it.
                      Avoid internal paths or test-only noise. At most one short hashtag if asked later.
                      Diff (JSON): """);
    private final String text;
    PushPrompts(String text) {
        this.text = text;
    }
    
    public String getText() {
        return this.text;
    }
}
