package org.cryptotrader.admin.ui;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.cryptotrader.admin.route.AppPage;

@Slf4j
@Component
public class NavItem extends BaseComponent {
    @FXML
    private Button actionButton;
    
    private final ObjectProperty<AppPage> page = new SimpleObjectProperty<>(this, "page", null);
    private final StringProperty navTitle = new SimpleStringProperty(this, "navTitle", "");
    public NavItem() {
        this.load();
    }
    
    
    @FXML
    public void initialize() {
//        this.actionButton.textProperty().bind(this.navTitleProperty());
        this.navTitle.bind(page.asString());
        this.actionButton.textProperty().bind(this.navTitle);
    }
    
    @FXML
    public void go() {
        log.info("Navigating to {}", this.navTitle.get());
    }
    
    public StringProperty navTitleProperty() {
        return this.navTitle;
    }
    public String getNavTitle() {
        return this.navTitleProperty().get();
    }
    public void setNavTitle(String value) {
        this.navTitleProperty().set(value);
    }

    public AppPage getPage() {
        return page.get();
    }

    public ObjectProperty<AppPage> pageProperty() {
        return page;
    }
    
    public void setPage(AppPage page) {
        this.page.set(page);
    }
}
