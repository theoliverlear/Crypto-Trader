package org.theoliverlear.entity;
//=================================-Imports-==================================
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

    //============================-Overrides-=================================

    //------------------------------Equals------------------------------------

    //------------------------------Hash-Code---------------------------------

    //------------------------------To-String---------------------------------

    //=============================-Getters-==================================

    //=============================-Setters-==================================
}
