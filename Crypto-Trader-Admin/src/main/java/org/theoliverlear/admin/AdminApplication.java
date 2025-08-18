package org.theoliverlear.admin;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.theoliverlear.admin.config.SpringBootConfig;

import java.io.IOException;

public class AdminApplication extends Application {
    private ConfigurableApplicationContext applicationContext;
    
    @Override
    public void init() {
        this.applicationContext = new SpringApplicationBuilder(SpringBootConfig.class)
                                      .headless(false)
                                      .run();
    }
    
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(AdminApplication.class.getResource("ui/admin-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }
    
    @Override
    public void stop() {
        this.applicationContext.close();
    }
}
