package CryptoTraderV2.CoinbaseV2;

import CryptoTraderV2.Coinbase.DataParser;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Base64;
import java.util.HashMap;
import java.util.StringTokenizer;

public class CoinbaseConnection {
CoinbaseSignature signature;
HashMap<String, String> headers;
String baseURL;
String pageData;
    public CoinbaseConnection(CoinbaseSignature signature, String baseURL) {
        this.signature = signature;
        this.baseURL = baseURL;
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
    public void connect() throws IOException {
        URL url = new URL(this.baseURL + this.signature.getEndpointURL());
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setRequestMethod(this.signature.getMethodCall());
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

    public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeyException, IOException {
        String filaDataPath = "C:\\Users\\olive\\OneDrive\\Documents\\Key Folder\\CoinbaseAPI.txt";
        FileDataRetriever keyRetriever = new FileDataRetriever(1, filaDataPath);
        FileDataRetriever secretRetriever = new FileDataRetriever(2, filaDataPath);
        String timestamp = generateTimestamp();
        String methodCall = "GET";
        String hashAlgorithm = "HmacSHA256";
        String baseURL = "https://api.coinbase.com";
        //String requestPath = "/v2/exchange-rates?currency=USD";
        String requestPath = "/v2/accounts";
        String body = "";
        CoinbaseSignature signature = new CoinbaseSignature(keyRetriever.getData(),
                                                            secretRetriever.getData(),
                                                            timestamp,
                                                            methodCall,
                                                            hashAlgorithm,
                                                            requestPath,
                                                            body);
        CoinbaseConnection connection = new CoinbaseConnection(signature, baseURL);
        connection.connect();
        DataParser parsePageData = new DataParser(connection.getPageData());
        System.out.println(parsePageData.getRawData());
        System.out.println(parsePageData.getData());
        System.out.println(parsePageData.getJsonMap());
        System.out.println(parsePageData.getJsonMap().get("id"));

    }
}
