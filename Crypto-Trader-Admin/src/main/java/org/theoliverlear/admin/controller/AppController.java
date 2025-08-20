package org.theoliverlear.admin.controller;

import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.theoliverlear.admin.component.ViewLoader;
import org.theoliverlear.admin.event.PageNavigationEvent;
import org.theoliverlear.admin.route.AppPage;
import org.theoliverlear.admin.ui.NavBar;

@Slf4j
@Component
public class AppController extends BaseViewController {
    @FXML private NavBar navBar;
    @FXML private StackPane page;

    private final ViewLoader viewLoader;
    
    @Autowired
    public AppController(ViewLoader viewLoader) {
        this.viewLoader = viewLoader;
    }

    @FXML
    private void initialize() {

    }
    
    @EventListener
    public void onNavigate(PageNavigationEvent event) {
        log.info("Navigation event received");
        Class<?> controllerClass;
        switch (event.appPage()) {
            case AppPage.ADMIN -> controllerClass = AdminController.class;
            case AppPage.AUTH -> controllerClass = AuthController.class;
            default -> throw new IllegalArgumentException("Unknown appPage: " + event.appPage());
        }
        this.viewLoader.loadView(this.page, controllerClass);
    }
}
