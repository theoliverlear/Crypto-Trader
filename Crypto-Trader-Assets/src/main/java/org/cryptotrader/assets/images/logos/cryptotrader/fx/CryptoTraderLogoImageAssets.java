package org.cryptotrader.assets.images.logos.cryptotrader.fx;

import javafx.scene.image.Image;
import org.cryptotrader.assets.util.ImageResource;

public class CryptoTraderLogoImageAssets {
    private static final String baseDirectory = "/assets/images/logos/crypto_trader/";
    private static final ImageResource imageLoader = new ImageResource();
    public static final Image LOGO_PNG = imageLoader.getImage(baseDirectory + "crypto_trader_logo.png");
    public static final Image CROPPED_PNG = imageLoader.getImage(baseDirectory + "crypto_trader_logo_cropped.png");
    public static final Image CROPPED_TRANSPARENT_PNG = imageLoader.getImage(baseDirectory + "crypto_trader_logo_cropped_transparent.png");
}
