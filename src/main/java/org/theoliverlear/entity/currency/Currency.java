package org.theoliverlear.entity.currency;
//=================================-Imports-==================================
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.theoliverlear.entity.currency.builder.CurrencyBuilder;
import org.theoliverlear.model.http.ApiDataRetriever;
import java.text.DecimalFormat;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "currencies")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "currencyCode")
@AllArgsConstructor
public class Currency {
    // TODO: Make a change calculator, static or nay, that takes two
    //       currencies, or passes one with this keyword, and returns the
    //       difference price and percentage change.
    //============================-Variables-=================================
    @Column(name = "currency_name")
    private String name;
    @Id
    @Column(name = "currency_code")
    private String currencyCode;
    private String urlPath;
    @Column(name = "currency_value", columnDefinition = "DECIMAL(34, 18)")
    private double value;
    @Column(name = "currency_value_formatted")
    private String formattedValue;
    @JsonIgnore
    @Transient
    private static final DecimalFormat decimalFormat = new DecimalFormat("##,#00.00000000");
    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;
    //===========================-Constructors-===============================
    public Currency() {
        this.name = "";
        this.currencyCode = "";
        this.urlPath = "";
        this.value = 0;
        this.formattedValue = "";
        this.lastUpdated = LocalDateTime.now();
    }
    public Currency(String name, String currencyCode) {
        this.name = name;
        this.currencyCode = currencyCode;
        this.urlPath = this.getCoinbaseUrl();
        this.value = this.getApiValue();
        this.formattedValue = decimalFormat.format(this.value);
        if (this.formattedValue == null) {
            this.formattedValue = "";
        }
        this.lastUpdated = LocalDateTime.now();
    }
    public Currency(String name, String currencyCode, String urlPath) {
        this.name = name;
        this.currencyCode = currencyCode;
        this.urlPath = urlPath;
        this.value = this.getApiValue();
        this.formattedValue = decimalFormat.format(this.value);
        if (this.formattedValue == null) {
            this.formattedValue = "";
        }
        this.lastUpdated = LocalDateTime.now();
    }
    public Currency(String name, String currencyCode, double value, String urlPath) {
        this.name = name;
        this.currencyCode = currencyCode;
        this.value = value;
        this.urlPath = urlPath;
        this.formattedValue = decimalFormat.format(value);
        if (this.formattedValue == null) {
            this.formattedValue = "";
        }
        this.lastUpdated = LocalDateTime.now();
    }
    public Currency(String name, String currencyCode, String urlPath, double value, LocalDateTime lastUpdated) {
        this.name = name;
        this.currencyCode = currencyCode;
        this.urlPath = urlPath;
        this.value = value;
        this.formattedValue = decimalFormat.format(value);
        if (this.formattedValue == null) {
            this.formattedValue = "";
        }
        this.lastUpdated = lastUpdated;
    }
    //=============================-Methods-==================================

    public String getCoinbaseUrl() {
        return "https://api.coinbase.com/v2/prices/%s-USD/spot".formatted(this.currencyCode);
    }
    //----------------------------Update-Value--------------------------------
    public void updateValue() {
        this.lastUpdated = LocalDateTime.now();
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
        String reformattedValue = decimalFormat.format(value);
        if (reformattedValue == null) {
            reformattedValue = "";
        }
        return reformattedValue;
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
    public static Currency from(Currency currency) {
        Currency newCurrency = new Currency(currency.getName(), currency.getCurrencyCode(),
                currency.getUrlPath(), currency.getValue(),
                currency.getLastUpdated());
        return newCurrency;
    }
    //============================-Overrides-=================================

    //------------------------------Equals------------------------------------
    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object instanceof Currency comparedCurrency) {
            boolean sameName = this.name.equals(comparedCurrency.name);
            boolean sameCode = this.currencyCode.equals(comparedCurrency.currencyCode);
            boolean sameValue = this.value == comparedCurrency.value;
            boolean sameUrl = this.urlPath.equals(comparedCurrency.urlPath);
            boolean sameLastUpdated = this.lastUpdated.equals(comparedCurrency.lastUpdated);
            return sameName && sameCode && sameValue && sameUrl && sameLastUpdated;
        }
        return false;
    }
    //------------------------------Hash-Code---------------------------------

    //------------------------------To-String---------------------------------
    @Override
    public String toString() {
        String currencyString = """
                %18s --- %5s - $%16s""".formatted(this.name, this.currencyCode,
                                          this.formattedValue);
        return currencyString;
    }
    public static CurrencyBuilder builder() {
        return new CurrencyBuilder();
    }
    //=============================-Getters-==================================
    public DecimalFormat getDecimalFormat() {
        return decimalFormat;
    }
    //=============================-Setters-==================================
}
