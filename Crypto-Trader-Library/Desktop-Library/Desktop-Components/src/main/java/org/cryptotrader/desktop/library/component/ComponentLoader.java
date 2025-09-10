package org.cryptotrader.desktop.library.component;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import org.cryptotrader.desktop.component.config.SpringContext;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;

@Component
public class ComponentLoader {
    public void loadWithFxRoot(Object controller, Parent root) {
        String fxmlPath = this.resolveFxmlPath(controller.getClass());
        URL resource = Objects.requireNonNull(controller.getClass().getResource(fxmlPath), "FXML not found: " + fxmlPath);

        AutowireCapableBeanFactory acb = SpringContext.getContext().getAutowireCapableBeanFactory();
        acb.autowireBean(controller);
        
        FXMLLoader loader = new FXMLLoader(resource);
        loader.setRoot(root);
        loader.setControllerFactory(type ->
                type.isInstance(controller) ? controller : SpringContext.getContext().getBean(type));
        try {
            loader.load();
        } catch (IOException ex) {
            throw new IllegalStateException("Failed to load FXML for " + controller.getClass().getName(), ex);
        }
        String cssPath = fxmlPath.replace(".fxml", ".css");
        URL css = controller.getClass().getResource(cssPath);
        if (css != null) {
            root.getStylesheets().add(css.toExternalForm());
        }
    }

    public Parent loadAsChild(Object controller) {
        String fxmlPath = this.resolveFxmlPath(controller.getClass());
        URL resource = Objects.requireNonNull(controller.getClass().getResource(fxmlPath), "FXML not found: " + fxmlPath);

        FXMLLoader loader = new FXMLLoader(resource);
        loader.setController(controller);
        loader.setControllerFactory(type ->
                type.isInstance(controller) ? controller : SpringContext.getContext().getBean(type));

        try {
            Parent root = loader.load();
            String cssPath = fxmlPath.replace(".fxml", ".css");
            URL css = controller.getClass().getResource(cssPath);
            if (css != null) {
                root.getStylesheets().add(css.toExternalForm());
            }
            return root;
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load FXML for " + controller.getClass().getName(), e);
        }
    }

    public void loadIntoPane(Object controller, Pane container) {
        Parent child = loadAsChild(controller);
        container.getChildren().setAll(child);
    }

    private String resolveFxmlPath(Class<?> componentClass) {
        String packageName = componentClass.getPackageName();
        if (!packageName.contains(".ui")) {
            throw new IllegalArgumentException("Component must be in a '.ui' package: " + packageName);
        }
        String simpleName = componentClass.getSimpleName();
        String kebabFolder = simpleName.replaceAll("([a-z])([A-Z]+)", "$1-$2").toLowerCase();
        packageName = packageName.replace('.', '/').replace("/ui", "/ui/component");
        return "/" + packageName + "/" + kebabFolder + "/" + simpleName + ".fxml";
    }
}
