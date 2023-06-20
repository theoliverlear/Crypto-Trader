package CryptoTraderV2.Coinbase;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
//import java.net.http.*;
//
//import java.net.http.HttpClient;
//import java.net.http.HttpHeaders;
//import java.net.http.HttpRequest;
//import java.net.http.HttpResponse;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Scanner;
import java.io.*;

public class CoinbaseAPI_V2 {
    String userID;
    public static final String HMAC_SHA256_ALGORITHM = "HmacSHA256";
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
    public CoinbaseAPI_V2() throws IOException, InterruptedException, NoSuchAlgorithmException, InvalidKeyException {
        String path = "https://api.coinbase.com/v2/accounts";
        String apiKey = "";
        String secretPhrase = "";
        String passphrase = "";


        String timestamp =  String.valueOf(System.currentTimeMillis() / 1000);
        String cbTimeStamp = this.getTimestamp();

        File keyDoc = new File("C:\\Users\\olive\\OneDrive\\Documents\\Key Folder\\CoinbaseAPI.txt");
        Scanner fileReader = new Scanner(keyDoc);
        while (fileReader.hasNext()) {
            String name = fileReader.next();
            apiKey = fileReader.next();
            secretPhrase = fileReader.next();
            break;
        }
        fileReader.close();

        String extensionOne = "/v2/accounts";
        String extensionTwo = "/v2/user/auth";


        String prehash = timestamp + "GET" + extensionTwo;

        String signature = generateCBAccessSign(secretPhrase, prehash);
//        this.userID = this.getUserID(apiKey, timestamp, signature);
//        System.out.println(ANSI_BLACK_BACKGROUND + ANSI_YELLOW + "userID: " + this.userID + ANSI_RESET);
        URL apiURL = new URL(path);


        String pathOne = "https://api.coinbase.com/v2/accounts";
        String pathTwo = "https://api.coinbase.com/v2/user/auth";
        String pathTwoShortened = "https://api.coinbase.com/v2/user";
        String pathThree = "https://api.coinbase.com/v2/time";
        String userID = this.getUserIDTwo(apiKey, timestamp, signature);
    }
    public String getTimestamp() throws IOException {
        URL timeURL = new URL("https://api.coinbase.com/v2/time");
        HttpURLConnection timeConnection = (HttpURLConnection) timeURL.openConnection();
        timeConnection.setRequestMethod("GET");
        BufferedReader timeParser = new BufferedReader(new InputStreamReader(timeConnection.getInputStream()));
        String timeReadWrite;
        String timestamp = "";
        while ((timeReadWrite = timeParser.readLine()) != null) {
            //timestamp = readWrite.substring(10, 20);
            timestamp = timeReadWrite.substring(timeReadWrite.indexOf("epoch") +
                            "epoch".length() + 2, timeReadWrite.length() - 2).trim()
                    .replace("\n", "").replace("\r", "");
        }
        timeParser.close();
        timeConnection.disconnect();
        return timestamp;
    }
    public static String generateCBAccessSign(String secret, String prehash) throws NoSuchAlgorithmException, InvalidKeyException {
        byte[] secretDecoded = Base64.getDecoder().decode(secret);

        SecretKeySpec secretKeySpec = new SecretKeySpec(secretDecoded, HMAC_SHA256_ALGORITHM);
        Mac mac = Mac.getInstance(HMAC_SHA256_ALGORITHM);
        mac.init(secretKeySpec);

        mac.update(prehash.getBytes());

        byte[] macData = mac.doFinal();

        String sign = Base64.getEncoder().encodeToString(macData);
        return sign;
    }
    public String getUserIDTwo(String apiKey, String timestamp, String signature) {
        String userID = "";
        try {
            String path = "https://api.coinbase.com/v2/user";
            URL apiURL = new URL(path);
            HttpURLConnection connection = (HttpURLConnection) apiURL.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("CB-ACCESS-KEY", apiKey);
            connection.setRequestProperty("CB-ACCESS-SIGN", signature);
            connection.setRequestProperty("CB-ACCESS-TIMESTAMP", timestamp);
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader parser = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String readWrite;
                StringBuilder pageData = new StringBuilder();
                while ((readWrite = parser.readLine()) != null) {
                    pageData.append(readWrite + "\n");
                    System.out.println(readWrite);
                }
                parser.close();
                connection.disconnect();
                userID = pageData.toString();
                return userID;
            } else {
                System.out.println("Error: " + responseCode + " " + connection.getResponseMessage());
            }
        } catch (IOException err) {
            err.printStackTrace();
        }
        return userID;
    }
//    public String getUserID(String apiKey, String timestamp, String signature) {
//        String userID = "";
//        try {
//            String path = "https://api.coinbase.com/v2/user";
//
//            HttpClient client = HttpClient.newBuilder().build();
//            HttpRequest.Builder builder = HttpRequest.newBuilder();
//            builder.uri(URI.create(path));
//            builder.header("CB-ACCESS-KEY", apiKey);
//            builder.header("CB-ACCESS-SIGN", signature);
//            builder.header("CB-ACCESS-TIMESTAMP", timestamp);
//            builder.GET();
//
//            HttpRequest request = builder
//                    .build();
//            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
//            int statusCode = response.statusCode();
//            if (statusCode == 200) {
//                String responseBody = response.body();
//                userID = responseBody;
//                System.out.println(responseBody);
//            } else {
//                System.out.println("Error: " + statusCode + " " + response.body());
//            }
//
//            HttpHeaders headers = response.headers();
//            headers.map().forEach((k, v) -> System.out.println(k + ":" + v));
//        } catch (IOException | InterruptedException err) {
//            err.printStackTrace();
//        }
//        return userID;
//    }
    public static void main(String[] args) throws IOException, InterruptedException, NoSuchAlgorithmException, InvalidKeyException {
        CoinbaseAPI_V2 coinbaseAPI = new CoinbaseAPI_V2();
    }
}
