module org.theoliverlear.admin {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;

    requires spring.context;
    requires spring.boot;
    requires spring.boot.autoconfigure;
    requires spring.core;
    requires spring.beans;
    requires spring.aop;

    requires org.apache.httpcomponents.client5.httpclient5;
    requires org.apache.httpcomponents.core5.httpcore5;
    requires org.slf4j;
    
    requires static lombok;
    requires spring.web;

    opens org.theoliverlear.admin to javafx.fxml, spring.core, spring.beans, spring.context;

    opens org.theoliverlear.admin.config;
    
    exports org.theoliverlear.admin;
    exports org.theoliverlear.admin.controller;
    opens org.theoliverlear.admin.controller to javafx.fxml, spring.beans, spring.context, spring.core;
}