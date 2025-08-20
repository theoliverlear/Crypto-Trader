package org.theoliverlear.admin.ui;

import jakarta.annotation.PostConstruct;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import org.springframework.stereotype.Component;
import org.theoliverlear.admin.config.SpringContext;
import org.theoliverlear.admin.model.Loadable;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;

@Component
public abstract class BaseComponent extends HBox implements Loadable {
    protected FXMLLoader loader;
    protected BaseComponent() {
        String fxmlPath = this.resolveFxmlPath(this.getClass());
        URL resource = Objects.requireNonNull(this.getClass().getResource(fxmlPath), "FXML not found: " + fxmlPath);
        FXMLLoader loader = new FXMLLoader(resource);
        loader.setRoot(this);
        loader.setControllerFactory(requestedType -> {
            if (requestedType.isInstance(this)) {
                return this;
            }
            return SpringContext.getContext().getBean(requestedType);
        });

        this.loader = loader;
        
        String cssPath = fxmlPath.replace(".fxml", ".css");
        URL cssResource = this.getClass().getResource(cssPath);
        if (cssResource != null) {
            getStylesheets().add(cssResource.toExternalForm());
        }
    }
    
    @Override
    public void load() {
        try {
            this.loader.load();
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to load FXML for " + this.getClass().getName(), exception);
        }
    }

    private String resolveFxmlPath(Class<?> componentClass) {
        String packageName = componentClass.getPackageName();
        if (!packageName.contains(".ui")) {
            throw new IllegalArgumentException("Component must be in a '.ui' package: " + packageName);
        }
        String simpleName = componentClass.getSimpleName();
        String kebabFolder = simpleName.replaceAll("([a-z])([A-Z]+)", "$1-$2").toLowerCase();
        packageName = packageName.replace('.', '/');
        packageName = packageName.replace("/ui", "/ui/component");
        return "/" + packageName
                + "/" + kebabFolder
                + "/" + simpleName + ".fxml";
    }
}
