package CryptoTraderV1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;

public class Currency implements Comparable <Currency>{
    DecimalFormat decimalFormat = new DecimalFormat("##,#00.00000000");
    String name;
    String currencyCode;
    double value;
    String formattedValue;
    String apiURL;
    public Currency(String name, String currencyCode, String apiURL) {
        this.apiURL = apiURL;
        this.name = name;
        this.currencyCode = currencyCode;
        double valueFromAPI = this.getValueAPI();
        this.value = valueFromAPI;
        this.formattedValue = this.formatValue(valueFromAPI);
    }
    public double getValueAPI() {
        StringBuilder currencyJSON = new StringBuilder();
        try {
            URL url = new URL(this.apiURL);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            BufferedReader input = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String jsonLine;
            while ((jsonLine = input.readLine()) != null) {
                currencyJSON.append(jsonLine);
            }
            input.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        currencyJSON.delete(0, currencyJSON.indexOf("amount"));
        currencyJSON.delete(currencyJSON.indexOf("}"), currencyJSON.length());
        currencyJSON.replace(0, currencyJSON.length(), currencyJSON.toString().replace("\"", ""));
        String[] amountTokenizer = currencyJSON.toString().split(":");
        StringBuilder valueFormat = new StringBuilder(String.format("%6.8f", Double.parseDouble(amountTokenizer[1])));

        return Double.parseDouble(valueFormat.toString());
    }
    public String formatValue(double value) {
        int difference = 6;
        String tempFormattedValue = decimalFormat.format(value);
        String[] tempFormattedValueSplit = tempFormattedValue.split("\\.");
        StringBuilder formattedValueBuilder = new StringBuilder();
        if (tempFormattedValueSplit[0].length() < difference) {
            formattedValueBuilder.append(tempFormattedValue);
            for (int i = 0; i < difference - tempFormattedValueSplit[0].length(); i++) {
                formattedValueBuilder.insert(0, " ");
            }
        } else {
            formattedValueBuilder.append(tempFormattedValue);
        }
        formattedValueBuilder.insert(0, "$");
        return formattedValueBuilder.toString();
    }
    public double getValue() {
        return this.value;
    }
    public void setValue(double value) {
        this.value = value;
    }
    public String getFormattedValue() {
        return this.asFormattedString(this.value);
    }
    public String getName() {
        return this.name;
    }
    public String getUpdatedFormattedValue() {
        this.updateValue();
        return this.formattedValue;
    }
    public double getUpdatedValue() {
        this.updateValue();
        return this.getValue();
    }
    public void updateValue() {
        StringBuilder currencyJSON = new StringBuilder();
        try {
            URL url = new URL(this.getApiURL());
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            BufferedReader input = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String jsonLine;
            while ((jsonLine = input.readLine()) != null) {
                currencyJSON.append(jsonLine);
            }
            input.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        currencyJSON.delete(0, currencyJSON.indexOf("amount"));
        currencyJSON.delete(currencyJSON.indexOf("}"), currencyJSON.length());
        currencyJSON.replace(0, currencyJSON.length(), currencyJSON.toString().replace("\"", ""));
        String[] amountTokenizer = currencyJSON.toString().split(":");
        StringBuilder valueFormat = new StringBuilder(String.format("%6.8f", Double.parseDouble(amountTokenizer[1])));
        this.setValue(Double.parseDouble(valueFormat.toString()));
    }
    public String getApiURL() {
        return this.apiURL;
    }
    public String asFormattedString(double value) {
        double valueFormat = Double.parseDouble(String.format("%6.8f", value));
        String tempFormattedValue = "$" + decimalFormat.format(valueFormat);
        return tempFormattedValue;
    }
    @Override
    public String toString() {
        String nameWithCode = this.name + " " + "(" + this.currencyCode + "): ";
        String currency = this.getFormattedValue();
        String buffer = "";
        int textWidth = 35;
        int totalSpace = nameWithCode.length() + currency.length();
        int difference;
        if (totalSpace < textWidth) {
            difference = textWidth - totalSpace;
            for (int i = 0; i < difference; i++) {
                buffer += " ";
            }
        }
        return nameWithCode + buffer + currency;
    }

    @Override
    public int compareTo(Currency comparedCurrency) {
        return Double.compare(this.value, comparedCurrency.value);
    }
}
