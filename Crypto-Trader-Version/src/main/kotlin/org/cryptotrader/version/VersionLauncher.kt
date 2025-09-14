package org.cryptotrader.version

import javafx.application.Application

class VersionLauncher

fun main(args: Array<String>) {
    try {
        val svgLoaderClass = Class.forName("de.codecentric.centerdevice.javafxsvg.SvgImageLoaderFactory")
        val method = svgLoaderClass.getMethod("install")
        method.invoke(null)
    } catch (throwable: Throwable) {
        System.err.println("[WARN] SVG loader not installed: ${throwable::class.java.name}: ${throwable.message}")
    }
    Application.launch(VersionApplication::class.java, *args)
}