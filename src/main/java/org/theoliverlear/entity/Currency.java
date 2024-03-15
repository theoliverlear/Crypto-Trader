package org.theoliverlear.entity;
//=================================-Imports-==================================
import org.theoliverlear.model.http.ApiDataRetriever;

import java.text.DecimalFormat;

public class Currency {
    //============================-Variables-=================================
    String name, currencyCode, urlPath, formattedValue;
    double value;
    DecimalFormat decimalFormat = new DecimalFormat("##,#00.00000000");
    //===========================-Constructors-===============================
    public Currency(String name, String currencyCode, double value, String urlPath) {
        this.name = name;
        this.currencyCode = currencyCode;
        this.value = value;
        this.urlPath = urlPath;
        this.formattedValue = this.decimalFormat.format(value);
    }
    //=============================-Methods-==================================
    public String formatValue(double value) {
        return this.decimalFormat.format(value);
    }
    public String getCurrencyApiJson() {
        ApiDataRetriever apiDataRetriever = new ApiDataRetriever(this.urlPath);
        return apiDataRetriever.getResponse();
    }
    public double getValueFromJson(String json) {
        StringBuilder currencyJson = new StringBuilder(json);
        int indexOfAmount = currencyJson.indexOf("amount");
        int endIndex = currencyJson.length();
        currencyJson.delete(0, indexOfAmount);
        int indexOfCloseCurly = currencyJson.indexOf("}");
        currencyJson.delete(indexOfCloseCurly, endIndex);
        String quoteRemove = currencyJson.toString().replace("\"", "");
        String[] keyValueSplit = quoteRemove.split(":");
        String currencyValueString = keyValueSplit[1];
        double currencyValue = Double.parseDouble(currencyValueString);
        return currencyValue;
    }
    //============================-Overrides-=================================

    //------------------------------Equals------------------------------------

    //------------------------------Hash-Code---------------------------------

    //------------------------------To-String---------------------------------

    //=============================-Getters-==================================

    //=============================-Setters-==================================
}
