package org.cryptotrader.version.script;

import lombok.extern.slf4j.Slf4j;
import org.cryptotrader.version.model.CryptoTraderModules;
import org.cryptotrader.version.model.Module;
import org.cryptotrader.version.model.config.ConfigFileType;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.input.SAXBuilder;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class PomParser {
    private static List<String> skipDirs = List.of("target", "node_modules", ".angular", "logs", ".git");
    private static Path rootPath = Path.of("..");
    public static List<Module> getAllPoms() {
        List<Module> modules;
        List<Path> pomPaths = getPomPaths();
        modules = pomPaths.stream().map(PomParser::getPom).collect(Collectors.toList());
        return modules;
    }

    public static Module getPom(Path pomPath) {
        try {
            Document doc = new SAXBuilder().build(pomPath.toFile());
            Element project = doc.getRootElement();
            Namespace namespace = project.getNamespace();

            ConfigFileType fileType = ConfigFileType.POM;
            String modulePathDir = getModulePath(pomPath);
            Path modulePath = Path.of(modulePathDir);
            CryptoTraderModules module = CryptoTraderModules.resolveFromPath(modulePath);
            return new Module(module, modulePath, fileType);
        } catch (IOException | JDOMException e) {
            throw new RuntimeException(e);
        }

    }


    public static String getModulePath(Path path) {
        String modulePath = path.toString();
        modulePath = modulePath.replace("..", "Crypto-Trader");
        modulePath = modulePath.replace("\\pom.xml", "");
        return modulePath;
    }

    public static List<Path> getPomPaths() {
        List<Path> paths = new ArrayList<>();
        try {
            Files.walkFileTree(rootPath, new SimpleFileVisitor<>() {
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
                    Path name = dir.getFileName();
                    if (name != null && skipDirs.contains(name.toString())) {
                        return FileVisitResult.SKIP_SUBTREE;
                    }
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                    if (attrs.isRegularFile() && "pom.xml".equals(file.getFileName().toString())) {
                        paths.add(file);
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException exception) {
            throw new IllegalStateException("Error in searching for pom.xml files.", exception);
        }
        return paths;
    }
}
