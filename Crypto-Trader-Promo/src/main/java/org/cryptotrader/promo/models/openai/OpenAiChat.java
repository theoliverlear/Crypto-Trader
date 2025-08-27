package org.cryptotrader.promo.models.openai;


import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.ChatModel;
import com.openai.models.chat.completions.ChatCompletion;
import com.openai.models.chat.completions.ChatCompletionCreateParams;

public class OpenAiChat {
    private static String apiKey = System.getenv("OPENAI_KEY");
    private OpenAIClient client;
    
    public OpenAiChat() {
        this.client = OpenAIOkHttpClient.builder()
                                        .apiKey(apiKey)
                                        .build();
    }
    
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
        ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
                                                                      .addUserMessage(userMessage)
                                                                      .addSystemMessage(systemMessage)
                                                                      .model(ChatModel.GPT_5)
                                                                      .build();
        ChatCompletion chatCompletion = this.client.chat().completions().create(params);
        return chatCompletion.choices().getFirst().message().content().orElseThrow(() -> {
            return new IllegalStateException("No content provided in OpenAI response.");
        });
    }
}

