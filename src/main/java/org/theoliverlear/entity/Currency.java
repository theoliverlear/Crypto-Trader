package org.theoliverlear.entity;
//=================================-Imports-==================================
import org.theoliverlear.model.http.ApiDataRetriever;

import java.text.DecimalFormat;

public class Currency {
    // TODO: Instead of enums, perhaps have static constants instantiated
    //       during runtime.
    // TODO: Make a change calculator, static or nay, that takes two
    //       currencies, or passes one with this keyword, and returns the
    //       difference price and percentage change.
    //============================-Variables-=================================
    private String name;
    private String currencyCode;
    private String urlPath;
    private String formattedValue;
    private double value;
    private DecimalFormat decimalFormat = new DecimalFormat("##,#00.00000000");
    //===========================-Constructors-===============================
    public Currency(String name, String currencyCode, String urlPath) {
        this.name = name;
        this.currencyCode = currencyCode;
        this.urlPath = urlPath;
        this.value = this.getApiValue();
        this.formattedValue = this.decimalFormat.format(this.value);
    }
    public Currency(String name, String currencyCode, double value, String urlPath) {
        this.name = name;
        this.currencyCode = currencyCode;
        this.value = value;
        this.urlPath = urlPath;
        this.formattedValue = this.decimalFormat.format(value);
    }
    //=============================-Methods-==================================

    //----------------------------Update-Value--------------------------------
    public void updateValue() {
        this.value = this.getApiValue();
        this.formattedValue = this.formatValue(this.value);
    }
    //---------------------------Get-Api-Value--------------------------------
    public double getApiValue() {
        String currencyApiJson = this.getCurrencyApiJson();
        return this.getValueFromJson(currencyApiJson);
    }
    //----------------------------Format-Value--------------------------------
    public String formatValue(double value) {
        return this.decimalFormat.format(value);
    }
    //-----------------------Get-Currency-Api-Json----------------------------
    public String getCurrencyApiJson() {
        ApiDataRetriever apiDataRetriever = new ApiDataRetriever(this.urlPath);
        return apiDataRetriever.getResponse();
    }
    //------------------------Get-Value-From-Json-----------------------------
    public double getValueFromJson(String json) {
        StringBuilder currencyJson = new StringBuilder(json);
        String amountKey = "amount\":\"";
        int amountLength = amountKey.length();
        int indexOfAmount = currencyJson.indexOf(amountKey) + amountLength;
        int endIndex = currencyJson.length();
        currencyJson.delete(0, indexOfAmount);
        int indexOfCloseQuote = currencyJson.indexOf("\"");
        currencyJson.delete(indexOfCloseQuote, endIndex);
        String currencyValueString = currencyJson.toString();
        double currencyValue = Double.parseDouble(currencyValueString);
        return currencyValue;
    }
    //-------------------------Get-Updated-Value------------------------------
    public double getUpdatedValue() {
        this.updateValue();
        return this.value;
    }
    //============================-Overrides-=================================

    //------------------------------Equals------------------------------------

    //------------------------------Hash-Code---------------------------------

    //------------------------------To-String---------------------------------
    @Override
    public String toString() {
        String currencyString = """
                %s - %s - $%s""".formatted(this.name, this.currencyCode,
                                          this.formattedValue);
        return currencyString;
    }
    //=============================-Getters-==================================
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
        return this.formattedValue;
    }
    public double getValue() {
        return this.value;
    }
    //=============================-Setters-==================================
    public void setName(String name) {
        this.name = name;
    }
    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }
    public void setUrlPath(String urlPath) {
        this.urlPath = urlPath;
    }
    public void setValue(double value) {
        this.value = value;
    }
    public void setFormattedValue(String formattedValue) {
        this.formattedValue = formattedValue;
    }

}
