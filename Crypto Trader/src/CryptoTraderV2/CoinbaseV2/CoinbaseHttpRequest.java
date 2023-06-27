package CryptoTraderV2.CoinbaseV2;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;

public class CoinbaseHttpRequest {
    HttpClient httpClient;
    HttpRequest httpRequest;
    public CoinbaseHttpRequest() {
        this.httpClient = HttpClient.newBuilder().build();
        this.httpRequest = HttpRequest.newBuilder().build();
    }
    public void addHeaders() {

    }
}
