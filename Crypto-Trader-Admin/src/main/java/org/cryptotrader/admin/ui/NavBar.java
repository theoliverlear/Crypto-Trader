package org.cryptotrader.admin.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.cryptotrader.admin.config.SpringContext;
import org.cryptotrader.admin.event.PageNavigationEvent;
import org.cryptotrader.admin.route.AppPage;

@Slf4j
@Component
public class NavBar extends BaseComponent {
    @FXML
    private Button authButton;

    private ApplicationEventPublisher events;

    public NavBar() {
        this.load();
    }
    
    @Autowired
    public NavBar(ApplicationEventPublisher events) {
        this.events = events;
    }
    

    private ApplicationEventPublisher events() {
        if (this.events == null) {
            this.events = SpringContext.getContext().getBean(ApplicationEventPublisher.class);
        }
        return this.events;
    }


    @FXML
    private void goAuth() {
        log.info("Navigating to Auth appPage");
        this.events().publishEvent(new PageNavigationEvent(AppPage.AUTH));
    }
}
