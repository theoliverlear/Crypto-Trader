package org.cryptotrader.version.library.model.config;

public enum ConfigFileType {
    POM("pom.xml"),
    PACKAGE("package.json"),
    PYPROJECT("pyproject.toml");
    
    private final String name;

    ConfigFileType(final String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
