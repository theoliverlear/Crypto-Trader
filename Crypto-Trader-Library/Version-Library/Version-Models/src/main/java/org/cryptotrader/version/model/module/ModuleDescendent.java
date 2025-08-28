package org.cryptotrader.version.model.module;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.cryptotrader.version.model.config.ConfigFileType;

import java.nio.file.Path;

@NoArgsConstructor
public class ModuleDescendent extends Module {
    private Module parent;
    private Module child;

    public ModuleDescendent(CryptoTraderModules moduleType,
                            Path modulePath,
                            ConfigFileType configFileType) {
        super(moduleType, modulePath, configFileType);
        this.parent = null;
        this.child = null;
    }
    
    public ModuleDescendent(CryptoTraderModules moduleType,
                            Path modulePath,
                            ConfigFileType configFileType,
                            Module parent,
                            Module child) {
        super(moduleType, modulePath, configFileType);
        this.parent = parent;
        this.child = child;
    }
}
