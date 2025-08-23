package org.cryptotrader.admin.ui;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.shape.SVGPath;
import lombok.extern.slf4j.Slf4j;
import org.cryptotrader.admin.component.ComponentLoader;
import org.cryptotrader.admin.config.SpringContext;
import org.cryptotrader.assets.images.icons.fx.IconImageAssets;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ExitAnchor extends HBox {
    @FXML
    ImageView anchorImage;
    
    public ExitAnchor() {
        SpringContext.getBean(ComponentLoader.class).loadWithFxRoot(this, this);
    }
    
    @FXML
    void initialize() {
        this.anchorImage.setImage(IconImageAssets.EXIT_DOOR_SVG);
        this.anchorImage.setPreserveRatio(true);
        this.anchorImage.setSmooth(true);
        this.anchorImage.setFitWidth(80);
        this.anchorImage.setFitHeight(80);
    }
    
    @FXML
    private void exit() {
        log.info("Exiting application");
        System.exit(0);
    }
}
