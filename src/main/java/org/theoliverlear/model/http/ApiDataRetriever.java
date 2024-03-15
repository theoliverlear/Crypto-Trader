package org.theoliverlear.model.http;
//=================================-Imports-==================================
import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;

public class ApiDataRetriever {
    //============================-Variables-=================================
    String url;
    String response;
    //============================-Constants-=================================
    static final String NO_DATA_ERROR_MESSAGE = "Error: No data received " +
                                                "from API";
    //===========================-Constructors-===============================
    public ApiDataRetriever(String url) {
        this.url = url;
        this.response = "";
        this.fetchResponse();
    }
    //=============================-Methods-==================================
    public void fetchResponse() {
        StringBuilder responseJson = new StringBuilder();
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
                responseJson.append(jsonLine);
            }
        } catch (URISyntaxException | IOException ex) {
            ex.printStackTrace();
        } finally {
            if (responseJson.isEmpty()) {
                this.response = NO_DATA_ERROR_MESSAGE;
                System.out.println(NO_DATA_ERROR_MESSAGE);
            } else {
                this.response = responseJson.toString();
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
    //============================-Overrides-=================================

    //------------------------------Equals------------------------------------

    //------------------------------Hash-Code---------------------------------

    //------------------------------To-String---------------------------------

    //=============================-Getters-==================================
    public String getResponse() {
        return this.response;
    }
    //=============================-Setters-==================================
}
