package org.theoliverlear.coinbasev2;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
//import java.net.http.HttpClient;
//import java.net.http.HttpRequest;
//import java.net.http.HttpResponse;
//import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import static java.nio.charset.StandardCharsets.UTF_8;

public class CoinbaseSignature {
    String apiKey;
    String apiSecret;
    String timestamp;
    String methodCall;
    String hashAlgorithm;
    String endpointURL;
    String messagePhrase;
    String signature;
    String body;
    public CoinbaseSignature(String apiKey,
                             String apiSecret,
                             String timestamp,
                             String methodCall,
                             String hashAlgorithm,
                             String endpointURL,
                             String body) throws NoSuchAlgorithmException,
                                                        InvalidKeyException {
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
        this.timestamp = timestamp;
        this.methodCall = methodCall;
        this.hashAlgorithm = hashAlgorithm;
        this.endpointURL = endpointURL;
        this.body = body;
        this.messagePhrase = this.generateMessagePhrase();
        this.signature = this.generateSignature();
        //this.setSignature(this.generateSignature());
    }
    public String generateMessagePhrase() {
        return this.getTimestamp() + this.methodCall + this.endpointURL + this.body;
    }
    public String getApiKey() {
        return this.apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getSignature() {
        return this.signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getEndpointURL() {
        return this.endpointURL;
    }

    public String getMethodCall() {
        return this.methodCall;
    }

    public String getApiSecret() {
        return this.apiSecret;
    }
    public String getMessagePhrase() {
        return this.messagePhrase;
    }

    public String generateSignature() throws NoSuchAlgorithmException,
                                             InvalidKeyException,
                                             NoSuchAlgorithmException {
        Mac authCode = Mac.getInstance(this.hashAlgorithm);
        // secretKey to bytes
        byte[] secretKeyInBytes = this.apiSecret.getBytes(UTF_8);
        // secretKey is encoded key
        SecretKeySpec secretKey = new SecretKeySpec(secretKeyInBytes, this.hashAlgorithm);
        // initialize authCode with secretKey
        authCode.init(secretKey);
        // messagePhrase to bytes
        byte[] messagePhraseInBytes = this.messagePhrase.getBytes(UTF_8);
        // update authCode with messagePhrase
        //authCode.update(messagePhraseInBytes);
        // authCode is the signature
        byte[] signatureInBytes = authCode.doFinal(messagePhraseInBytes);
        // convert signature to hexadecimal
        String signatureInHex = this.bytesToHex(signatureInBytes);
        return signatureInHex;
    }
    public String bytesToHex(byte[] bytes) {
        // Twice as big because each byte is represented by two characters
        StringBuilder hexString = new StringBuilder(2 * bytes.length);
        for (byte b : bytes) {
            // Convert each byte to a hexadecimal
            hexString.append(String.format("%02x", b));
        }
        return hexString.toString();
    }
}
