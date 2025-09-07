package org.cryptotrader.version.model.config;

import lombok.Getter;
import lombok.Setter;
import org.cryptotrader.version.model.dependency.Dependency;

import java.nio.file.Path;

@Getter
@Setter
public class ConfigFile {
    private ConfigFileType type;
    private Path filePath;
    private Dependency dependency;
    
    public ConfigFile() {
        this.type = null;
        this.filePath = null;
        this.dependency = null;
    }
    
    public ConfigFile(ConfigFileType type, Path filePath, Dependency dependency) {
        this.type = type;
        this.filePath = filePath;
        this.dependency = dependency;
    }
}
