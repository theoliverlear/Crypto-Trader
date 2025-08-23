package org.cryptotrader.admin.ui;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import lombok.extern.slf4j.Slf4j;
import org.cryptotrader.admin.component.ComponentLoader;
import org.cryptotrader.admin.config.SpringContext;
import org.cryptotrader.admin.event.PageNavigationEvent;
import org.cryptotrader.admin.route.AppPage;
import org.cryptotrader.assets.images.logos.cryptotrader.fx.CryptoTraderLogoImageAssets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class HomeAnchor extends HBox {
    @FXML
    private ImageView anchorImage;

    @Autowired
    private ApplicationEventPublisher events;

    public HomeAnchor() {
        SpringContext.getBean(ComponentLoader.class).loadWithFxRoot(this, this);
    }
    
    @FXML
    private void initialize() {
        this.anchorImage.setImage(CryptoTraderLogoImageAssets.CROPPED_TRANSPARENT_PNG);
        this.anchorImage.setFitWidth(100);
        this.anchorImage.setFitHeight(100);
    }
    
    @FXML
    public void navigateHome() {
        log.info("Navigating to home.");
        this.events.publishEvent(new PageNavigationEvent(AppPage.ADMIN_USERS));
    }
}
