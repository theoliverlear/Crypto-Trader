package org.cryptotrader.admin.controller;

import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import lombok.extern.slf4j.Slf4j;
import org.cryptotrader.desktop.component.ViewLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
//import org.cryptotrader.admin.component.ViewLoader;
import org.cryptotrader.admin.event.PageNavigationEvent;
import org.cryptotrader.admin.route.AppPage;
import org.cryptotrader.admin.ui.NavBar;

@Slf4j
@Component
public class AppController extends BaseViewController {
    @FXML private NavBar navBar;
    @FXML private VBox page;
    @FXML private BorderPane root;

    private final ViewLoader viewLoader;
    
    @Autowired
    public AppController(ViewLoader viewLoader) {
        this.viewLoader = viewLoader;
    }

    @FXML
    private void initialize() {
        this.onNavigate(new PageNavigationEvent(AppPage.AUTH));
    }
    
    @EventListener
    public void onNavigate(PageNavigationEvent event) {
        log.info("Navigation event received");
        Class<?> controllerClass;
        switch (event.appPage()) {
            case AppPage.ADMIN_USERS -> controllerClass = AdminUsersController.class;
            case AppPage.AUTH -> controllerClass = AuthController.class;
            default -> throw new IllegalArgumentException("Unknown appPage: " + event.appPage());
        }
        this.viewLoader.loadView(this.page, controllerClass);
    }
}
