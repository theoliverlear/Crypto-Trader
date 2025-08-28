package org.cryptotrader.version.model.element;

import lombok.NoArgsConstructor;
import org.cryptotrader.version.model.config.ConfigFileType;
import org.cryptotrader.version.model.dependency.type.PomDependency;
import org.cryptotrader.version.model.module.CryptoTraderModules;
import org.cryptotrader.version.model.module.type.Pom;
import org.jdom2.Element;
import org.jdom2.Namespace;


import java.nio.file.Path;
import java.util.Optional;

@NoArgsConstructor
public class PomElement {
    private Element baseElement;
    private Path path;
    
    public PomElement(Element baseElement, Path path) {
        this.baseElement = baseElement;
        this.path = path;
    }

    public String textFromNamespace(String name) {
        return this.baseElement.getChild(name, this.baseElement.getNamespace()).getText();
    }
    
    public Optional<PomElement> getParent() {
        Element parent = this.baseElement.getChild("parent", this.baseElement.getNamespace());
        if (parent == null) {
            return Optional.empty();
        } else {
            return Optional.of(new PomElement(parent, this.path));
        }
    }
    public Pom getPom() {
        String groupId;
        if (this.getParent().isPresent()) {
            groupId = this.getParent().get().textFromNamespace("groupId");
        } else {
            groupId = this.textFromNamespace("groupId");
        }
        ConfigFileType fileType = ConfigFileType.POM;
        String modulePathDir = getModulePath();
        Path modulePath = Path.of(modulePathDir);
        String name = this.textFromNamespace("name");
        String artifactId = this.textFromNamespace("artifactId");
        String version = this.textFromNamespace("version");
        CryptoTraderModules module = CryptoTraderModules.resolveFromPath(modulePath);
        PomDependency moduleDependency = new PomDependency(name, version, groupId, artifactId);
        Pom pom = new Pom(module, modulePath, fileType, moduleDependency);
        return pom;
    }

    public String getModulePath() {
        String modulePath = this.path.toString();
        modulePath = modulePath.replace("..", "Crypto-Trader");
        modulePath = modulePath.replace("\\pom.xml", "");
        return modulePath;
    }
}
