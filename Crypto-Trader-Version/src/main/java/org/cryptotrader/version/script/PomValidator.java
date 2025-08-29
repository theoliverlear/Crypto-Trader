package org.cryptotrader.version.script;

import org.cryptotrader.version.model.element.PomElement;
import org.cryptotrader.version.model.module.CryptoTraderModules;
import org.cryptotrader.version.model.module.type.Pom;

import java.util.List;

public class PomValidator {

    private static final List<PomElement> allPoms = PomParser.getAllPoms();
    public static boolean cryptoTraderParentMatches() {
        List<PomPair> pomPairs = List.of(
                new PomPair(getByModule(CryptoTraderModules.CRYPTO_TRADER), CryptoTraderModules.CRYPTO_TRADER),
                new PomPair(getByModule(CryptoTraderModules.LIBRARY), CryptoTraderModules.LIBRARY),
                new PomPair(getByModule(CryptoTraderModules.API_LIBRARY), CryptoTraderModules.API_LIBRARY),
                new PomPair(getByModule(CryptoTraderModules.DESKTOP_LIBRARY), CryptoTraderModules.DESKTOP_LIBRARY),
                new PomPair(getByModule(CryptoTraderModules.EXTERNALS_LIBRARY), CryptoTraderModules.EXTERNALS_LIBRARY),
                new PomPair(getByModule(CryptoTraderModules.VERSION_LIBRARY), CryptoTraderModules.VERSION_LIBRARY)
        );
        boolean allMatch = pomPairs.stream().allMatch(pomPair -> {
            boolean parentMatches = parentMatches(pomPair.element(), pomPair.module());
            System.out.println(pomPair.module() + " parent matches: " + parentMatches);
            return parentMatches;
        });
        return allMatch;
    }

    private static PomElement getByModule(CryptoTraderModules module) {
        PomElement element = allPoms.stream()
                .filter(pomElement -> pomElement.getPom().getModuleType() == module)
                .filter(pomElement -> "pom".equalsIgnoreCase(pomElement.getPackaging()))
                .findFirst()
                .orElseGet(() -> allPoms.stream()
                        .filter(pomElement -> pomElement.getPom().getModuleType() == module)
                        .findFirst()
                        .orElse(null));
        return element;
    }

    public static boolean parentMatches(PomElement topPom, CryptoTraderModules module) {
        if (topPom == null) {
            return false;
        }
        String topVersion = topPom.getVersion();
        System.out.println("Expected version: " + topVersion + " for " + module);
        boolean allMatch = true;
        for (PomElement pomElement : allPoms) {
            Pom parentPom = pomElement.getParentPomModel();
            if (parentPom != null) {
                if (parentPom.getModuleType() == module) {
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
    private record PomPair(PomElement element, CryptoTraderModules module) { }

    private static String nullToEmpty(String text) {
        return text == null ? "" : text;
    }
}
