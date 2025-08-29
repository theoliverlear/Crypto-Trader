package org.cryptotrader.version.model.module.type;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.cryptotrader.version.model.config.ConfigFileType;
import org.cryptotrader.version.model.dependency.type.PomDependency;
import org.cryptotrader.version.model.module.CryptoTraderModules;
import org.cryptotrader.version.model.module.Module;
import org.cryptotrader.version.model.module.ModuleDescendent;

import java.nio.file.Path;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Getter
public class Pom extends ModuleDescendent {
    private PomDependency moduleDependency;
    public Pom(CryptoTraderModules moduleType,
               Path modulePath,
               ConfigFileType configFileType,
               PomDependency moduleDependency) {
        super(moduleType, modulePath, configFileType);
        this.moduleDependency = moduleDependency;
    }
    public Pom(CryptoTraderModules moduleType,
               Path modulePath,
               ConfigFileType configFileType,
               PomDependency moduleDependency,
               Module parent,
               Module child) {
        super(moduleType, modulePath, configFileType, parent, child);
        this.moduleDependency = moduleDependency;
    }
}
