package org.cryptotrader.admin;

import de.codecentric.centerdevice.javafxsvg.SvgImageLoaderFactory;
import javafx.application.Application;

public class AdminLauncher {
    public static void main(String[] args) {
        SvgImageLoaderFactory.install();
        Application.launch(AdminApplication.class, args);
    }
}
