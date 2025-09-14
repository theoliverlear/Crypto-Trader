open module org.cryptotrader.version {
    requires static lombok;
    requires org.jdom2;
    requires org.slf4j;
    requires java.xml;
    requires org.cryptotrader.version.library.model;
    requires org.apache.commons.io;
    requires kotlin.stdlib;
    requires com.sigwarthsoftware.changelog;
    requires org.jetbrains.annotations;
    requires com.sigwarthsoftware.promo;
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;
    requires spring.boot;
    requires spring.boot.autoconfigure;
    requires spring.context;
    requires spring.beans;
    requires spring.core;
    requires spring.jcl;
    requires org.yaml.snakeyaml;
    requires java.sql;
    requires javafxsvg;
    requires net.rgielen.fxweaver.core;
    requires org.cryptotrader.assets;
    requires org.cryptotrader.desktop.library.components;
    requires fr.brouillard.oss.cssfx;

    exports org.cryptotrader.version;
    exports org.cryptotrader.version.script;
}