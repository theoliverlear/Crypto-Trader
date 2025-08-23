package org.cryptotrader.assets.images.icons.fx;

import javafx.scene.image.Image;
import org.cryptotrader.assets.util.ImageResource;

public class IconImageAssets {
    private static final String baseDirectory = "/assets/images/icons/";
    private static final ImageResource imageLoader = new ImageResource();
    
    public static final Image EXIT_DOOR_SVG = imageLoader.getImage(baseDirectory + "exit_door_icon.svg");
}
