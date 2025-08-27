open module org.cryptotrader.desktop.components {
    requires javafx.fxml;
    requires javafx.graphics;
    requires static lombok;
    requires spring.beans;
    requires spring.context;
    requires org.slf4j;
    
    exports org.cryptotrader.desktop.component;
    exports org.cryptotrader.desktop.component.config;
}