package org.cryptotrader.admin.controller;

import javafx.fxml.FXML;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import org.springframework.stereotype.Component;

@Component
public class EmailController extends VBox {
    private static final String WEBMAIL_URL = "https://sscryptotrader.awsapps.com/mail";

    @FXML
    private WebView webView;

    public EmailController() {

    }

    @FXML
    private void initialize() {
        // Let the WebView grow and shrink within its parent
        VBox.setVgrow(webView, Priority.ALWAYS);
        webView.setMinSize(0, 0);

        if (webView.getParent() instanceof Region parent) {
            webView.prefWidthProperty().bind(parent.widthProperty());
            webView.prefHeightProperty().bind(parent.heightProperty());
        }

        this.webView.getEngine().load(WEBMAIL_URL);
    }
}
