package org.cryptotrader.version.script;

import org.cryptotrader.version.library.model.element.PomElement;
import org.cryptotrader.version.library.model.element.PomPair;
import org.cryptotrader.version.library.model.module.ModuleLibrary;
import org.cryptotrader.version.library.model.module.type.Pom;

import java.util.Arrays;
import java.util.List;

public class PomValidator {
    private static final List<PomElement> allPoms = PomParser.getAllPoms();
    private static final List<PomPair> allPomPairs = getAllPomPairs();
    
    
    public static List<PomPair> getAllPomPairs() {
        
        List<PomPair> pomPairs = allPoms.stream().map(pomElement -> {
            String name = pomElement.getPom().getModuleDependency().getName();
            // These will likely be important later. Deprecated currently.
            boolean rootChild = Arrays.asList(ModuleLibrary.cryptoTraderParent).contains(name);
            boolean libraryChild = Arrays.asList(ModuleLibrary.libraryParent).contains(name);
            ModuleLibrary libraryModule = ModuleLibrary.modules.stream()
                    .filter(module -> module.getName().equals(name))
                    .findFirst().orElseThrow(() -> new IllegalStateException("Module not found: " + name));
            return new PomPair(pomElement, libraryModule);
        }).toList();
        return pomPairs;
    }
    
    public static boolean allMatch() {
        return parentMatches(allPomPairs);
    }
    
    public static boolean parentMatches(List<PomPair> pomPairs) {
        boolean allMatch = pomPairs.stream().allMatch(pomPair -> {
            boolean parentMatches = parentMatches(pomPair.element(), pomPair.module());
            System.out.println(pomPair.module() + " parent matches: " + parentMatches);
            return parentMatches;
        });
        return allMatch;
    }

    private static PomElement getByModule(ModuleLibrary module) {
        PomElement element = allPoms.stream()
                .filter(pomElement -> pomElement.getPom().getModuleType().equals(module))
                .filter(pomElement -> "pom".equalsIgnoreCase(pomElement.getPackaging()))
                .findFirst()
                .orElseGet(() -> allPoms.stream()
                        .filter(pomElement -> pomElement.getPom().getModuleType().equals(module))
                        .findFirst()
                        .orElse(null));
        return element;
    }

    public static boolean parentMatches(PomElement topPom, ModuleLibrary module) {
        if (topPom == null) {
            return false;
        }
        String topVersion = topPom.getVersion();
        System.out.println("Expected version: " + topVersion + " for " + module);
        boolean allMatch = true;
        for (PomElement pomElement : allPoms) {
            Pom parentPom = pomElement.getParentPomModel();
            if (parentPom != null) {
                if (parentPom.getModuleType().equals(module)) {
                    String parentVersion = parentPom.getModuleDependency().getVersion();
                    if (!nullToEmpty(parentVersion).equals(nullToEmpty(topVersion))) {
                        System.out.println("Found version: " + parentVersion);
                        return false;
                    }
                }
            }
        }
        return allMatch;
    }

    private static String nullToEmpty(String text) {
        return text == null ? "" : text;
    }
}
