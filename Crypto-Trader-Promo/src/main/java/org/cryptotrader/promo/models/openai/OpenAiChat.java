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
        systemMessage = "You are ReleaseBot. Write ONE tweet about user-visible improvements. No quotes." + systemMessage;
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

