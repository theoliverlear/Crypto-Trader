package org.cryptotrader.desktop.library.component.ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;
import org.cryptotrader.desktop.library.component.config.SpringContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.cryptotrader.desktop.library.component.model.Loadable;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;

@Component
@Scope("prototype")
@Lazy
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
            this.getStylesheets().add(cssResource.toExternalForm());
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
