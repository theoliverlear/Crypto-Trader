package org.cryptotrader.version.model.module;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.cryptotrader.version.model.config.ConfigFileType;

import java.nio.file.Path;

@Getter
@Setter
@Data
@NoArgsConstructor
public class Module {
    private CryptoTraderModules moduleType;
    private Path modulePath;
    private ConfigFileType configFileType;
    private String name;
    private String version;

    public Module(CryptoTraderModules moduleType,
                  Path modulePath,
                  ConfigFileType configFileType) {
        this.moduleType = moduleType;
        this.modulePath = modulePath;
        this.configFileType = configFileType;
    }
}
