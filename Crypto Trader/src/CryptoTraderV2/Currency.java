package CryptoTraderV2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
public class Currency {
    //----------------------------Class Variables-----------------------------
    String name, currencyCode, urlPath, formattedValue;
    double value;
    DecimalFormat decimalFormat = new DecimalFormat("##,#00.00000000");
    public Currency(String name, String currencyCode, String urlPath) {
        //----------------------Set Instance Variables------------------------
        this.name = name;
        this.currencyCode = currencyCode;
        this.urlPath = urlPath;
        //----------------------Set Format Required Variables-----------------
        double valueFromAPI = 0;
        try {
            valueFromAPI = this.getValueAPI();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.value = valueFromAPI;
        this.formattedValue = this.formatValue(valueFromAPI);
    }
    //========================-Get-Value-From-API-Method-=====================
    public double getValueAPI() throws IOException {
        // ------------------------Set Variables In Try Scope-----------------
        StringBuilder currencyJSON;
        HttpURLConnection urlConnection = null;
        BufferedReader apiReader = null;
        // ------------------------Get Data From API/URL----------------------
        try {
            //---------------------Create Connection Objects------------------
            URL url = new URL(this.urlPath);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            //--------------------------Read the Data-------------------------
            apiReader = new BufferedReader(new InputStreamReader(
                                       urlConnection.getInputStream()));
            currencyJSON = new StringBuilder();
            String jsonLine;
            while ((jsonLine = apiReader.readLine()) != null) {
                currencyJSON.append(jsonLine);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (apiReader != null) {
                apiReader.close();
            }
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        //-------------------------Format JSON Data---------------------------
        currencyJSON.delete(0, currencyJSON.indexOf("amount"));
        currencyJSON.delete(currencyJSON.indexOf("}"), currencyJSON.length());
        String backslashReplace = currencyJSON.toString().replace("\"", "");
        currencyJSON.replace(0, currencyJSON.length(), backslashReplace);
        String[] codeValue = currencyJSON.toString().split(":");
        double valueParsed = Double.parseDouble(codeValue[1]);
        String valueFormat = String.format("%6.8f", valueParsed);
        return Double.parseDouble(valueFormat);
    }
    //==========================-Format-Value-Method-=========================
    public String formatValue(double value) {
        //-----------------Declare Utility & Formatted Variables--------------
        int difference = 6;
        String withDecimalFormat = decimalFormat.format(value);
        String[] decimalPointSplit = withDecimalFormat.split("\\.");
        StringBuilder formatBuilder = new StringBuilder();
        int leftOfDecimalLength = decimalPointSplit[0].length();
        if (leftOfDecimalLength < difference) {
            formatBuilder.append(withDecimalFormat);
            int differenceExcess = difference - leftOfDecimalLength;
            for (int buffer = 0; buffer < differenceExcess; buffer++) {
                formatBuilder.insert(0, " ");
            }
        } else {
            formatBuilder.append(withDecimalFormat);
        }
        formatBuilder.insert(0, "$");
        return formatBuilder.toString();
    }
    //=====================-Update-Value-From-API-Method-=====================
    public void updateValue() throws IOException {
        // ------------------------Set Variables In Try Scope-----------------
        StringBuilder currencyJSON;
        HttpURLConnection urlConnection = null;
        BufferedReader apiReader = null;
        // ------------------------Get Data From API/URL----------------------
        try {
            //---------------------Create Connection Objects------------------
            URL url = new URL(this.urlPath);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            //--------------------------Read the Data-------------------------
            apiReader = new BufferedReader(new InputStreamReader(
                                       urlConnection.getInputStream()));
//            int responseCode = urlConnection.getResponseCode();
//            if (responseCode == 429) {
//                Thread.currentThread().sleep(100);
//            }
            currencyJSON = new StringBuilder();
            String jsonLine;
            while ((jsonLine = apiReader.readLine()) != null) {
                currencyJSON.append(jsonLine);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (apiReader != null) {
                apiReader.close();
            }
            if (urlConnection != null) {
                //urlConnection.disconnect();
            }
        }
        //-------------------------Format JSON Data---------------------------
        currencyJSON.delete(0, currencyJSON.indexOf("amount"));
        currencyJSON.delete(currencyJSON.indexOf("}"), currencyJSON.length());
        String backslashReplace = currencyJSON.toString().replace("\"", "");
        currencyJSON.replace(0, currencyJSON.length(), backslashReplace);
        String[] codeValue = currencyJSON.toString().split(":");
        double valueParsed = Double.parseDouble(codeValue[1]);
        String valueFormat = String.format("%6.8f", valueParsed);
        this.setValue(Double.parseDouble(valueFormat));
    }
    public String asFormattedString(double value) {
        double valueFormat = Double.parseDouble(String.format("%6.8f", value));
        String asString = "$" + decimalFormat.format(valueFormat);
        return asString;
    }
    //=============================-Getters-Setters-==========================
    public String getName() {
        return this.name;
    }
    public String getCurrencyCode() {
        return this.currencyCode;
    }
    public String getUrlPath() {
        return this.urlPath;
    }
    public String getFormattedValue() {
        return this.asFormattedString(this.value);
    }
    public double getValue() {
        return this.value;
    }
    public void setValue(double value) {
        this.value = value;
    }
    public boolean isSameDenomination(Currency currency) {
        return this.getName().equals(currency.getName());
    }
    public static boolean isSameDenominationStatic(Currency currencyOne, Currency currencyTwo) {
        return currencyOne.getName().equals(currencyTwo.getName());
    }
    public double getUpdatedValue() throws IOException {
        this.updateValue();
        return this.getValue();
    }
}
