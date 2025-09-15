package org.cryptotrader.admin;

import javafx.application.Application;

import java.lang.reflect.Method;

public class AdminLauncher {
    public static void main(String[] args) {
        attemptInitSvgFactory();
        Application.launch(AdminApplication.class, args);
    }

    private static void attemptInitSvgFactory() {
        try {
            Class<?> svgLoaderClass = Class.forName("de.codecentric.centerdevice.javafxsvg.SvgImageLoaderFactory");
            Method method = svgLoaderClass.getMethod("install");
            method.invoke(null);
        } catch (Throwable throwable) {
            System.err.println("[WARN] SVG loader not installed: " + throwable.getClass().getName() + ": " + throwable.getMessage());
        }
    }
}
