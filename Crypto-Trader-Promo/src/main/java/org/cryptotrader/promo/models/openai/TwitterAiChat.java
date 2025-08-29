package org.cryptotrader.promo.models.openai;

import org.cryptotrader.externals.openai.OpenAiChat;

public class TwitterAiChat extends OpenAiChat {
    public TwitterAiChat() {
        super();
    }

    @Override
    public String chat(String systemMessage, String userMessage) {
        systemMessage = """
             You are ReleaseBot, a concise release promo copywriter.
             Write ONE tweet about user-visible improvements. No quotes.
             You are providing updates about the app Crypto Trader.
             Include "Crypto Trader" in the post.
             """ + systemMessage + """
             Keep it upbeat but not cringey.
             Use Apple supported emojis where appropriate. Maximum 1 emoji per sentence.
             Return only the tweet text, no quotes. Max chars at 280.
             Emojis use 2 chars each and you must ensure that the total chars is no more than 280.
             """;
        return super.chat(systemMessage, userMessage);
    }
}
