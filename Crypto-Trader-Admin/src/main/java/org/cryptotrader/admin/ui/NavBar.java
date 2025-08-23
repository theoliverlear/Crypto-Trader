package org.cryptotrader.admin.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import lombok.extern.slf4j.Slf4j;
import org.cryptotrader.admin.component.ComponentLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.cryptotrader.admin.config.SpringContext;
import org.cryptotrader.admin.event.PageNavigationEvent;
import org.cryptotrader.admin.route.AppPage;

@Slf4j
@Component
public class NavBar extends HBox {
    @FXML
    private HomeAnchor homeAnchor;
    
    @FXML
    private ExitAnchor exitAnchor;
    
    @Autowired
    private ApplicationEventPublisher events;

    public NavBar() {
        SpringContext.getBean(ComponentLoader.class).loadWithFxRoot(this, this);
    }

    @FXML
    public void initialize() {
        
    }
}
