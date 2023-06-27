package CryptoTraderV2.CoinbaseV2;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.HashMap;

public class CoinbaseConnection {
CoinbaseSignature signature;
HashMap<String, String> headers;
String baseURL;
String pageData;
String timestamp;
String apiKey;
String apiSecret;
String hashAlgorithm;
String methodCall;
String endpointURL;
String body;
final String fileDataPath = "C:\\Users\\olive\\OneDrive\\Documents\\Key Folder\\CoinbaseAPI.txt";
    public CoinbaseConnection(String baseURL,
                              String methodCall,
                              String apiKey,
                              String apiSecret,
                              String hashAlgorithm,
                              String endpointURL,
                              String body) throws NoSuchAlgorithmException, InvalidKeyException {

        this.baseURL = baseURL;
        this.timestamp = generateTimestamp();
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
        this.hashAlgorithm = hashAlgorithm;
        this.methodCall = methodCall;
        this.endpointURL = endpointURL;
        this.body = body;

        this.signature = new CoinbaseSignature(this.apiKey,
                                               this.apiSecret,
                                               this.timestamp,
                                               this.methodCall,
                                               this.hashAlgorithm,
                                               this.endpointURL,
                                               this.body);
        this.pageData = "";
        this.headers = new HashMap<>();
        this.headers.put("CB-ACCESS-KEY", this.signature.getApiKey());
        this.headers.put("CB-ACCESS-SIGN", this.signature.getSignature());
        this.headers.put("CB-ACCESS-TIMESTAMP", this.signature.getTimestamp());
        this.headers.put("CB-Version", "2021-07-20");
        this.headers.put("Content-Type", "application/json");
        this.headers.put("Cache-Control", "no-cache");
        this.headers.put("X-Content-Type-Options", "nosniff");
        this.headers.put("X-Frame-Options", "deny");
        this.headers.put("Strict-Transport-Security", "max-age=3153600");
        this.headers.put("X-XSS-Protection", "1; mode=block");
    }
    public void setPageData(String pageData) {
        this.pageData = pageData;
    }
    public String getPageData() {
        return this.pageData;
    }
    public void setEndpointURL(String endpointURL) {
        this.endpointURL = endpointURL;
    }
    public void connect() throws IOException {
        URL url = new URL(this.baseURL + this.signature.endpointURL);
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setRequestMethod(this.signature.methodCall);
        connection.setInstanceFollowRedirects(false);

        for (String key : this.headers.keySet()) {
            connection.setRequestProperty(key, this.headers.get(key));
        }
        connection.connect();
        int responseCode = connection.getResponseCode();
        if (responseCode == HttpsURLConnection.HTTP_OK) {
            InputStreamReader inputStream = new InputStreamReader(connection.getInputStream());
            BufferedReader parser = new BufferedReader(inputStream);
            String readWrite;
            StringBuilder pageData = new StringBuilder();
            while ((readWrite = parser.readLine()) != null) {
                pageData.append(readWrite + "\n");
                //System.out.println(readWrite);
            }
            this.setPageData(pageData.toString());
            parser.close();
            connection.disconnect();
        } else {
            System.out.println("Error: " + responseCode + " " + connection.getResponseMessage());
            connection.disconnect();
        }
    }
    public static String generateTimestamp() {
        long time = Instant.now().getEpochSecond();
        return String.valueOf(time);
    }
}
