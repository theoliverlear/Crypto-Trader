package org.cryptotrader.promo.models.twitter;

import com.github.scribejava.apis.TwitterApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth10aService;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

@Slf4j
public class Tweeter {
    private TwitterEnvironment environment;
    private OAuth10aService authenticator;
    private OAuth1AccessToken token;

    public Tweeter() {
        this.environment = TwitterEnvironment.fromSystem();
        this.authenticator = new ServiceBuilder(this.environment.consumerKey())
                                                                .apiSecret(this.environment.consumerSecret())
                                                                .build(TwitterApi.instance());
        this.token = new OAuth1AccessToken(this.environment.accessToken(), this.environment.accessTokenSecret());
    }

    public OAuthRequest getRequest(String tweet) {
        OAuthRequest request = new OAuthRequest(Verb.POST, "https://api.x.com/2/tweets");
        request.addHeader("Content-Type", "application/json");
        request.setPayload("{\"text\":\"" + handleEscapes(tweet) + "\"}");
        this.authenticator.signRequest(this.token, request);
        return request;
    }
    
    public void tweet(String tweet) {
        try (Response response = this.authenticator.execute(this.getRequest(tweet))) {
            if (response.getCode() / 100 != 2) {
                log.error("Failed: {} {} {}", response.getCode(), response.getMessage(), response.getBody());
                return;
            }
            log.info("Tweeted: {}", tweet);
            
        } catch (IOException | InterruptedException | ExecutionException exception) {
            log.error("Failed to tweet: {} - {}", tweet, exception.toString());
        }
    }

    static String handleEscapes(String text) {
        return text.replace("\\", "\\\\")
                   .replace("\"", "\\\"");
    }
}
