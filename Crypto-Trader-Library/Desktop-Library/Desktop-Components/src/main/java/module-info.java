open module org.cryptotrader.desktop.components {
    requires javafx.base;
    requires javafx.fxml;
    requires javafx.graphics;
    requires static lombok;
    requires spring.core;
    requires spring.beans;
    requires spring.context;
    requires org.slf4j;
    requires kotlin.stdlib;
    
    exports org.cryptotrader.desktop.component;
    exports org.cryptotrader.desktop.component.config;
}