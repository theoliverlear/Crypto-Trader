package org.cryptotrader.promo.models.openai;

public enum IssuePrompts {
    SYSTEM("""
            Given a newly opened GitHub issue that proposes an idea or improvement, write ONE tweet that frames it as a positive, forward-looking milestone for Crypto Trader.
            - Treat it as upcoming work (not shipped yet). Prefer verbs like "planning", "working on", "aiming to", "exploring", "coming soon".
            - Summarize the user benefit in plain language; avoid internal jargon and file paths.
            - If the idea targets a specific area (website, desktop app, API, testing), mention it in a non-technical way (e.g., "the website module", "the API library") only if it helps clarity.
            - Optionally include at most ONE short hashtag related to the topic (derive from labels/tags if useful). If nothing fits, omit the hashtag.
            - Do not say "issue", "ticket", or "PR". Do not use quotes around the tweet. No links unless provided in the text.
            - Be confident but avoid overpromising.
            - Points are on a 10 point scale.
            - The higher the points, the bigger the feature.
            """),
    USER("""
            Summarize the essence from the title first, then enrich with the most relevant detail from the body.
            Emphasize the user-facing outcome or value ((in no order of importance) speed, reliability, new feature, clarity, quality, bug fix, etc.).
            If tags are provided, consider using ONE short, general hashtag derived from them. Use points context to highlight how big the benefit is but do not reference the amount as a number, but as a general tenor for the post.
            Issue details:
            """);
    
    private final String text;
    
    IssuePrompts(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }
}
