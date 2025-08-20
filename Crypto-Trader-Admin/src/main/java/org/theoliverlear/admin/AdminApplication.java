package org.theoliverlear.admin;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.theoliverlear.admin.config.SpringBootConfig;
import fr.brouillard.oss.cssfx.CSSFX;


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
        FXMLLoader fxmlLoader = new FXMLLoader(AdminApplication.class.getResource("ui/view/app/AppView.fxml"));
        fxmlLoader.setControllerFactory(this.applicationContext::getBean);
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        System.out.println("[Loaded Controller] " + fxmlLoader.getController()
                + " @" + System.identityHashCode(fxmlLoader.getController()));
        stage.setTitle("Crypto Trader Admin Panel");
        stage.setScene(scene);
        CSSFX.start();
        stage.show();
    }
    
    @Override
    public void stop() {
        this.applicationContext.close();
    }
}
