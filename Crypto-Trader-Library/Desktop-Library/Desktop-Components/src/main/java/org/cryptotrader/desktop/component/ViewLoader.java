package org.cryptotrader.desktop.component;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;

@Component
@Slf4j
public class ViewLoader {
    private final ApplicationContext applicationContext;

    @Autowired
    public ViewLoader(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public Parent initializeView(Class<?> controllerClass) {
        String fxmlPath = this.resolveFxmlPath(controllerClass);
        String cssPath = fxmlPath.replace(".fxml", ".css");
        URL resource = Objects.requireNonNull(controllerClass.getResource(fxmlPath));
        URL cssResource = controllerClass.getResource(cssPath);
        FXMLLoader loader = new FXMLLoader(resource);
        loader.setControllerFactory(this.applicationContext::getBean);
        try {
            Parent load = loader.load();
            if (cssResource != null) {
                load.getStylesheets().add(cssResource.toExternalForm());
            }
            return load;
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to load FXML: " + fxmlPath, exception);
        }
    }

    public void loadView(Pane container,
                         Class<?> controllerClass) {
        Parent view = this.initializeView(controllerClass);
        container.getChildren().setAll(view);
    }

    private String resolveFxmlPath(Class<?> controllerClass) {
        String simpleName = controllerClass.getSimpleName();
        String basePackage = extractBasePackage(controllerClass, simpleName);
        String feature = simpleName.substring(0, simpleName.length() - "Controller".length()).toLowerCase();
        String viewName = simpleName.replace("Controller", "View") + ".fxml";
        String resourcePackage = basePackage + ".ui.view." + feature;
        String resourcePath = "/" + resourcePackage.replace('.', '/') + "/" + viewName;
        return resourcePath;
    }

    private static String extractBasePackage(Class<?> controllerClass, String simpleName) {
        if (!isValidClass(simpleName)) {
            throw new IllegalArgumentException("Controller class must end with 'Controller': " + simpleName);
        }
        String packageName = controllerClass.getPackageName();
        int controllerIndex = packageName.lastIndexOf(".controller");
        if (controllerIndex < 0) {
            throw new IllegalArgumentException("Package must contain '.controller': " + packageName);
        }
        String basePackage = packageName.substring(0, controllerIndex);
        return basePackage;
    }

    private static boolean isValidClass(String simpleName) {
        return simpleName.endsWith("Controller");
    }
}
