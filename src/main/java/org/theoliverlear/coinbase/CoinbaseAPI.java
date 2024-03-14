package org.theoliverlear.coinbase;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class CoinbaseAPI {
    public static final String HMAC_SHA256_ALGORITHM = "HmacSHA256";
    String API_KEY;
    String API_SECRET;
    String API_PASSPHRASE;
    public CoinbaseAPI() throws IOException, NoSuchAlgorithmException, InvalidKeyException {

        Scanner fileReader = new Scanner(new File("C:\\Users\\olive\\OneDrive\\Documents\\Key Folder\\CoinbaseAPI.txt"));
        while (fileReader.hasNext()) {
            this.API_KEY = fileReader.next().trim().replace("\n", "").replace("\r", "");
            this.API_SECRET = fileReader.next().trim().replace("\n", "").replace("\r", "");
        }
        fileReader.close();
        System.out.println(this.API_KEY);
        System.out.println(this.API_SECRET);
        String requestPath = "/v2/user";
        String method = "GET";
        String body = "";


        //String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
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

        String prehash = timestamp + method + requestPath + body;
        System.out.println(prehash);
        String signature = generateCBAccessSign(this.API_SECRET, prehash);
        System.out.println(signature);
        String path = "https://api.coinbase.com" + requestPath;
        URL url = new URL(path);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();;
        connection.setRequestMethod(method);


        String divider = "--------------------------------------------------";
        System.out.println(divider);
        Map<String, List<String>> preConnectionMap = connection.getRequestProperties();
        for (Map.Entry<String, List<String>> entry : preConnectionMap.entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue());
            if (entry.getValue().size() > 1) {
                for (int i = 0; i < entry.getValue().size(); i++) {
                    System.out.println(entry.getValue().get(i));
                }
            }
        }
        System.out.println(divider);
        connection.setConnectTimeout(50000);
        connection.setReadTimeout(50000);
        connection.setDoInput(true);
        connection.setDoOutput(true);


        connection.addRequestProperty("Content-Type", "application/json");
        connection.addRequestProperty("CB-ACCESS-SIGN", signature);
        connection.addRequestProperty("CB-ACCESS-KEY", this.API_KEY);
        connection.addRequestProperty("CB-ACCESS-TIMESTAMP", timestamp);
        //connection.addRequestProperty("CB-ACCESS-PASSPHRASE", this.API_PASSPHRASE);


        //connection.setRequestProperty("Content-Type", "application/json");
//        connection.setRequestProperty("X-CB-ACCESS-SIGNATURE", signature);
//        connection.setRequestProperty("X-CB-ACCESS-KEY", this.API_KEY);
//        connection.setRequestProperty("X-CB-ACCESS-TIMESTAMP", timestamp);
        //connection.setRequestProperty("CB-ACCESS-PASSPHRASE", this.API_PASSPHRASE);


        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("CB-ACCESS-SIGN", signature);
        connection.setRequestProperty("CB-ACCESS-KEY", this.API_KEY);
        connection.setRequestProperty("CB-ACCESS-TIMESTAMP", timestamp);

        //connection.setRequestProperty("CB-ACCESS-PASSPHRASE", this.API_PASSPHRASE);

        System.out.println(divider);
        Map<String, List<String>> connectionMap = connection.getRequestProperties();
        for (Map.Entry<String, List<String>> entry : connectionMap.entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue());
            if (entry.getValue().size() > 1) {
                for (int i = 0; i < entry.getValue().size(); i++) {
                    System.out.println(entry.getValue().get(i));
                }
            }
        }
        System.out.println(divider);

        connection.connect();
        System.out.println("responseCode: " + connection.getResponseCode());
        System.out.println("responseMessage: " + connection.getResponseMessage());
        System.out.println("headerFields: " + connection.getHeaderFields());
        System.out.println("contentType: " + connection.getContentType());
        //System.out.println("content: " + connection.getContent());
        System.out.println("contentLength: " + connection.getContentLength());
        System.out.println("contentEncoding: " + connection.getContentEncoding());
        System.out.println("expiration: " + connection.getExpiration());
        System.out.println("date: " + connection.getDate());
        System.out.println("lastModified: " + connection.getLastModified());
        System.out.println("URL: " + connection.getURL());
        System.out.println("permission: " + connection.getPermission());
        System.out.println("connectTimeout: " + connection.getConnectTimeout());
        System.out.println("readTimeout: " + connection.getReadTimeout());
        System.out.println("allowUserInteraction: " + connection.getAllowUserInteraction());
        System.out.println("useCaches: " + connection.getUseCaches());
        System.out.println("doInput: " + connection.getDoInput());
        System.out.println("doOutput: " + connection.getDoOutput());
        System.out.println("method: " + connection.getRequestMethod());
        System.out.println("instanceFollowRedirects: " + connection.getInstanceFollowRedirects());
        System.out.println("connected: " + connection.getConnectTimeout());
        System.out.println("contentLengthLong: " + connection.getContentLengthLong());

//        int responseCode = connection.getResponseCode();
//        if (responseCode != HttpURLConnection.HTTP_OK) {
//            System.out.println("Error: " + responseCode + " " + connection.getResponseMessage());
//            connection.disconnect();
//            fileReader.close();
//        }

        InputStream stream = connection.getInputStream();
        BufferedReader parser = new BufferedReader(new InputStreamReader(stream));

        //BufferedReader parser = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String readWrite;
        while ((readWrite = parser.readLine()) != null) {
            System.out.println(readWrite);
        }
        parser.close();
        connection.disconnect();

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
    public static void main(String[] args) throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        CoinbaseAPI coinbaseAPI = new CoinbaseAPI();
    }
}
