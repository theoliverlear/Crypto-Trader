package org.cryptotrader.version.model.module;

import lombok.Getter;

import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;

@Getter
public class ModuleLibrary {
    public static String[] cryptoTraderParent = {
            "Crypto-Trader-Admin",
            "Crypto-Trader-Api",
            "Crypto-Trader-Assets",
            "Crypto-Trader-Coverage",
            "Crypto-Trader-Data",
            "Crypto-Trader-Docs",
            "Crypto-Trader-Engine",
            "Crypto-Trader-Health",
            "Crypto-Trader-Library",
            "Crypto-Trader-Logging",
            "Crypto-Trader-Mobile",
            "Crypto-Trader-Testing",
            "Crypto-Trader-Version"
    };
    
    public static String[] libraryParent = {
            "Api-Library",
            "Api-Communication",
            "Api-Components",
            "Api-Config",
            "Api-Models",
            "Api-Repositories",
            "Api-Services",
            "Desktop-Library",
            "Desktop-Components",
            "Externals-Library"
    };
    
    public static List<ModuleLibrary> modules = List.of(
            new ModuleLibrary("Crypto-Trader-Admin"),
            new ModuleLibrary("Crypto-Trader-Api"),
            new ModuleLibrary("Crypto-Trader-Assets"),
            new ModuleLibrary("Crypto-Trader-Coverage"),
            new ModuleLibrary("Crypto-Trader-Data"),
            new ModuleLibrary("Crypto-Trader-Docs"),
            new ModuleLibrary("Crypto-Trader-Engine"),
            new ModuleLibrary("Crypto-Trader-Health"),
            new ModuleLibrary("Crypto-Trader-Library"),
            new ModuleLibrary("Api-Library"),
            new ModuleLibrary("Api-Communication"),
            new ModuleLibrary("Api-Components"),
            new ModuleLibrary("Api-Config"),
            new ModuleLibrary("Api-Models"),
            new ModuleLibrary("Api-Repositories"),
            new ModuleLibrary("Api-Services"),
            new ModuleLibrary("Desktop-Library"),
            new ModuleLibrary("Desktop-Components"),
            new ModuleLibrary("Externals-Library"),
            new ModuleLibrary("Version-Library"),
            new ModuleLibrary("Version-Models"),
            new ModuleLibrary("Crypto-Trader-Logging"),
            new ModuleLibrary("Crypto-Trader-Mobile"),
            new ModuleLibrary("Crypto-Trader-Testing"),
            new ModuleLibrary("Crypto-Trader-Version"),
            new ModuleLibrary("Crypto-Trader")
    );
    
    private String name;

    public static void initializeModules(List<ModuleLibrary> modules) {
        ModuleLibrary.modules = modules;
    }

    public ModuleLibrary(String name) {
        this.name = name;
    }

    public static ModuleLibrary resolveFromPath(Path path) {
        if (path == null) {
            throw new IllegalArgumentException("Path must not be null");
        }
        if (modules == null || modules.isEmpty()) {
            throw new IllegalStateException("ModuleLibrary has not been initialized. Call ModuleLibrary.initializeModules(...) before resolving modules. Path=" + path);
        }
        String pathText = path.toString();
        String[] segments = pathText.split("[\\\\/]+");
        ModuleLibrary deepestSegmentMatch = null;
        int deepestIndex = -1;
        for (int i = 0; i < segments.length; i++) {
            String segment = segments[i];
            for (ModuleLibrary module : modules) {
                if (segment.equals(module.getName())) {
                    if (i > deepestIndex) {
                        deepestIndex = i;
                        deepestSegmentMatch = module;
                    }
                }
            }
        }
        if (deepestSegmentMatch != null) {
            return deepestSegmentMatch;
        }
        return modules.stream()
                .filter(module -> pathText.contains(module.getName()))
                .max(Comparator.comparingInt(module -> module.getName().length()))
                .orElseThrow(() -> new IllegalArgumentException("Unable to resolve module from path: " + path + ". Known modules=" + modules.stream().map(ModuleLibrary::getName).toList()));
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object instanceof ModuleLibrary module) {
            return this.name.equals(module.getName());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.name.hashCode();
    }
}
