package org.cryptotrader.admin;

import de.codecentric.centerdevice.javafxsvg.SvgImageLoaderFactory;
import de.codecentric.centerdevice.javafxsvg.dimension.PrimitiveDimensionProvider;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;
import org.cryptotrader.admin.dev.JRebelHook;
import org.cryptotrader.assets.images.logos.cryptotrader.fx.CryptoTraderLogoImageAssets;
import org.scenicview.ScenicView;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.cryptotrader.admin.config.SpringBootConfig;
import fr.brouillard.oss.cssfx.CSSFX;


import java.io.IOException;
import java.net.URL;

public class AdminApplication extends Application {
    private ConfigurableApplicationContext applicationContext;

    @Override
    public void init() {
        System.setProperty("spring.aop.proxy-target-class", "false");
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
        URL cssPath = AdminApplication.class.getResource("ui/view/app/AppView.css");
        if (cssPath != null) {
            scene.getStylesheets().add(cssPath.toExternalForm());
        }
        stage.setScene(scene);
        CSSFX.start();
        stage.show();
//        ScenicView.show(scene);
        stage.getIcons().add(CryptoTraderLogoImageAssets.CROPPED_TRANSPARENT_PNG);
        this.addReloadKeybind(scene);
        this.addJRebelListener(scene);
    }

    private void addJRebelListener(Scene scene) {
        JRebelHook.register(
                () -> reloadScene(scene),
                "org.cryptotrader.admin.controller",
                "org.cryptotrader.admin.ui"
        );
    }

    private void addReloadKeybind(Scene scene) {
        scene.getAccelerators().put(
                new KeyCodeCombination(KeyCode.R, 
                        KeyCombination.CONTROL_DOWN,
                        KeyCombination.SHIFT_DOWN),
                () -> {
                    reloadScene(scene);
                }
        );
    }


    public void reloadScene(Scene scene) {
        try {
            FXMLLoader reloadLoader = new FXMLLoader(
                    AdminApplication.class.getResource("ui/view/app/AppView.fxml"));
            reloadLoader.setControllerFactory(this.applicationContext::getBean);

            Parent newRoot = reloadLoader.load();
            scene.setRoot(newRoot);
//            ScenicView.show(newRoot);
            System.out.println("[Hot Reload] scene root swapped; controller = "
                    + reloadLoader.getController() + " @"
                    + System.identityHashCode(reloadLoader.getController()));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void stop() {
        this.applicationContext.close();
    }
}
