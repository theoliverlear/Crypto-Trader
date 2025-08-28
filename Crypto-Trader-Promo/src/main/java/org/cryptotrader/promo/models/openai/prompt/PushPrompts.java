package org.cryptotrader.promo.models.openai.prompt;

public enum PushPrompts {
    SYSTEM("""
                    Given a Git commit messages, write ONE tweet that highlights user-visible improvements (not filenames), avoids jargon, and reads naturally.
                    - If work is done specifically for a certain module (ex. Testing, Website, Api modules, etc.), specify it in the post.
                    - Refer to it in a non technical way like "the website module" or "the API library".
                    - Some specifics can be added as long as they are in layman's terms like "new desktop window" or "API services".
                    - Some readers may be developers, so sparingly throw in specific jargon if it fits in a general sense.
                    """),
    USER("""
            Focus on what a user gets ((in no order of importance) speed, reliability, new feature, clarity, quality, bug fix, etc.).
            Try to be as specific as you can while being approachable in tone.
            Find the tangible benefit and promote it.
            Avoid internal paths or test-only noise. 
            At most one short hashtag related to the commit aimed at maximizing reach.
            Most importantly, the Tweet must be relevant to the commit messages.
            Diff (commit messages): \n""");
    private final String text;
    PushPrompts(String text) {
        this.text = text;
    }
    
    public String getText() {
        return this.text;
    }
}
