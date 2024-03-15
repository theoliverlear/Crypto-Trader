package org.theoliverlear.model.http;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

public class ApiDataRetriever {
    String url;
    String response;
    public ApiDataRetriever(String url) {
        this.url = url;
        this.response = "";
        this.fetchResponse();
    }
    public void fetchResponse() {
        StringBuilder currencyJson = new StringBuilder();
        HttpsURLConnection urlConnection = null;
        BufferedReader apiReader = null;
        try {
            URI uri = new URI(this.url);
            urlConnection = (HttpsURLConnection) uri.toURL().openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            InputStream urlStream = urlConnection.getInputStream();
            InputStreamReader urlStreamReader = new InputStreamReader(urlStream);
            apiReader = new BufferedReader(urlStreamReader);
            String jsonLine;
            while ((jsonLine = apiReader.readLine()) != null) {
                currencyJson.append(jsonLine);
            }
        } catch (URISyntaxException | IOException ex) {
            ex.printStackTrace();
        } finally {
            if (currencyJson.length() == 0) {
                this.response = "Error: No data received from API";
            } else {
                this.response = currencyJson.toString();
            }
            this.shutDownConnections(urlConnection, apiReader);
        }
    }
    public void shutDownConnections(HttpsURLConnection urlConnection, BufferedReader apiReader) {
        if (apiReader != null) {
            try {
                apiReader.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        if (urlConnection != null) {
            urlConnection.disconnect();
        }
    }
    public String getResponse() {
        return this.response;
    }
}
