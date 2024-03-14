package org.theoliverlear.coinbase;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Scanner;

public class CoinbaseProAPI {
    String API_KEY;
    String API_SECRET;
    String API_PASSPHRASE;
    String API_URL;
    String API_VERSION;
    String API_METHOD;
    public static final String HMAC_SHA256_ALGORITHM = "HmacSHA256";

    public CoinbaseProAPI() throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        URL url = new URL("https://api.pro.coinbase.com/accounts");
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        Scanner fileReader = new Scanner(new File("C:\\Users\\olive\\OneDrive\\Documents\\Key Folder\\CoinbaseProAPI.txt"));
        while (fileReader.hasNext()) {
            this.API_KEY = fileReader.nextLine();
            this.API_PASSPHRASE = fileReader.nextLine();
            this.API_SECRET = fileReader.nextLine();
        }
        fileReader.close();
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        String method = connection.getRequestMethod();
        String requestPath = "/accounts";
        String prehash = timestamp + method + requestPath;

        String signature = generateCBAccessSign(this.API_SECRET, prehash);

        connection.setRequestProperty("CB-ACCESS-KEY", this.API_KEY);
        connection.setRequestProperty("CB-ACCESS-SIGN", signature);
        connection.setRequestProperty("CB-ACCESS-TIMESTAMP", timestamp);
        connection.setRequestProperty("CB-ACCESS-PASSPHRASE", this.API_PASSPHRASE);
        connection.setRequestProperty("Content-Type", "application/json");

        int responseCode = connection.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            connection.disconnect();
            throw new RuntimeException("Error: " + responseCode + " " + connection.getResponseMessage());
        }
        BufferedReader parser = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String readWrite;
        while ((readWrite = parser.readLine()) != null) {
            System.out.println(readWrite);
        }
        parser.close();
        connection.disconnect();
    }

    public static String generateCBAccessSign(String secret, String message) throws NoSuchAlgorithmException, InvalidKeyException {
        Mac mac = Mac.getInstance(HMAC_SHA256_ALGORITHM);
        SecretKeySpec secretKeySpec = new SecretKeySpec(Base64.getDecoder().decode(secret), HMAC_SHA256_ALGORITHM);
        mac.init(secretKeySpec);
        String sign = Base64.getEncoder().encodeToString(mac.doFinal(message.getBytes()));
        return sign;
    }
    public static void main(String[] args) throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        CoinbaseProAPI coinbaseProAPI = new CoinbaseProAPI();
    }
}
