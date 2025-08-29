package org.cryptotrader.version.model.element;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.cryptotrader.version.model.config.ConfigFileType;
import org.cryptotrader.version.model.dependency.type.PomDependency;
import org.cryptotrader.version.model.module.CryptoTraderModules;
import org.cryptotrader.version.model.module.type.Pom;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.input.SAXBuilder;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

@NoArgsConstructor
@Data
@Getter
public class PomElement {
    private static final Namespace MAVEN_NAMESPACE = Namespace.getNamespace("http://maven.apache.org/POM/4.0.0");
    private Optional<PomElement> parent;
    private Element baseElement;
    private Path path;

    public PomElement(Element baseElement, Path path) {
        this.baseElement = baseElement;
        this.path = path;
        this.parent = resolveParent();
    }
    
    public String textFromNamespace(String name) {
        Element child = this.baseElement.getChild(name, this.baseElement.getNamespace());
        if (child == null) {
            child = this.baseElement.getChild(name, MAVEN_NAMESPACE);
        }
        if (child == null) {
            child = this.baseElement.getChild(name);
        }
        if (child != null) {
            String text = child.getText();
            if (text != null && !text.isBlank()) {
                return text;
            }
        }
        return this.getParent().map(parent -> parent.textFromNamespace(name)).orElse("");
    }
    
    public String directText(String name) {
        Element child = this.baseElement.getChild(name, this.baseElement.getNamespace());
        if (child == null) {
            child = this.baseElement.getChild(name, MAVEN_NAMESPACE);
        }
        if (child == null) {
            child = this.baseElement.getChild(name);
        }
        return child == null ? "" : (child.getText() == null ? "" : child.getText());
    }

    public PomDependency getParentDependency() {
        Element parentEl = this.baseElement.getChild("parent", this.baseElement.getNamespace());
        if (parentEl == null) parentEl = this.baseElement.getChild("parent", MAVEN_NAMESPACE);
        if (parentEl == null) parentEl = this.baseElement.getChild("parent");
        if (parentEl == null) return null;
        String groupId = getChildText(parentEl, "groupId");
        String artifactId = getChildText(parentEl, "artifactId");
        String version = getChildText(parentEl, "version");
        String name = "parent";
        return new PomDependency(name, version, groupId, artifactId);
    }

    public Pom getParentPomModel() {
        PomDependency coords = this.getParentDependency();
        if (coords == null) return null;
        Optional<PomElement> possibleParent = this.getParent();
        if (possibleParent.isEmpty()) {
            return null;
        }
        PomElement parentElement = possibleParent.get();
        Path modulePath = Path.of(parentElement.getModulePath());
        ConfigFileType fileType = ConfigFileType.POM;
        CryptoTraderModules module = CryptoTraderModules.resolveFromPath(modulePath);
        return new Pom(module, modulePath, fileType, coords);
    }

    private String getChildText(Element element, String name) {
        Element child = element.getChild(name, element.getNamespace());
        if (child == null) child = element.getChild(name, MAVEN_NAMESPACE);
        if (child == null) child = element.getChild(name);
        return child == null ? "" : (child.getText() == null ? "" : child.getText());
    }
    
    private Optional<PomElement> resolveParent() {
        Element parentElement = this.baseElement.getChild("parent", this.baseElement.getNamespace());
        if (parentElement == null) parentElement = this.baseElement.getChild("parent", MAVEN_NAMESPACE);
        if (parentElement == null) parentElement = this.baseElement.getChild("parent");
        if (parentElement == null) {
            return Optional.empty();
        }
        Element relativePath = parentElement.getChild("relativePath", parentElement.getNamespace());
        if (relativePath == null) relativePath = parentElement.getChild("relativePath", MAVEN_NAMESPACE);
        if (relativePath == null) relativePath = parentElement.getChild("relativePath");
        String relativePathText = null;
        boolean relPresent = false;
        if (relativePath != null) {
            relPresent = true;
            String text = relativePath.getText();
            if (text != null && !text.isBlank()) {
                relativePathText = text.trim();
            } else {
                return Optional.empty();
            }
        }
        if (!relPresent) {
            relativePathText = "..\\pom.xml";
        }
        Path parentPomPath = this.path.getParent().resolve(relativePathText).normalize();
        try {
            Document doc = new SAXBuilder().build(parentPomPath.toFile());
            Element project = doc.getRootElement();
            return Optional.of(new PomElement(project, parentPomPath));
        } catch (IOException | JDOMException e) {
            return Optional.empty();
        }
    }

    public String getVersion() {
        return this.textFromNamespace("version");
    }

    public String getPackaging() {
        return this.directText("packaging");
    }

    public Pom getPom() {
        ConfigFileType fileType = ConfigFileType.POM;
        Path modulePath = Path.of(getModulePath());
        String name = this.directText("name");
        String artifactId = this.directText("artifactId");
        String version = this.directText("version");
        String groupId = this.directText("groupId");
        CryptoTraderModules module = CryptoTraderModules.resolveFromPath(modulePath);
        PomDependency moduleDependency = new PomDependency(name, version, groupId, artifactId);
        return new Pom(module, modulePath, fileType, moduleDependency);
    }

    public String getModulePath() {
        String modulePath = this.path.toString();
        modulePath = modulePath.replace("..", "Crypto-Trader");
        modulePath = modulePath.replace("\\pom.xml", "");
        return modulePath;
    }
}
