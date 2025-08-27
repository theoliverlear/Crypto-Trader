package org.cryptotrader.version.model;

import lombok.Getter;
import lombok.Setter;
import org.cryptotrader.version.model.config.ConfigFileType;

import java.nio.file.Path;

@Getter
@Setter
public class Module {
    private CryptoTraderModules moduleType;
    private Path modulePath;
    private ConfigFileType configFileType;
}
