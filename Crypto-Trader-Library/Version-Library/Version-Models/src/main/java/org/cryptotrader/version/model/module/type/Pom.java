package org.cryptotrader.version.model.module.type;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.cryptotrader.version.model.config.ConfigFileType;
import org.cryptotrader.version.model.dependency.type.PomDependency;
import org.cryptotrader.version.model.module.ModuleLibrary;
import org.cryptotrader.version.model.module.ProgramModule;
import org.cryptotrader.version.model.module.ProgramModuleDescendent;

import java.nio.file.Path;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Getter
public class Pom extends ProgramModuleDescendent {
    private PomDependency moduleDependency;
    public Pom(ModuleLibrary moduleType,
               Path modulePath,
               ConfigFileType configFileType,
               PomDependency moduleDependency) {
        super(moduleType, modulePath, configFileType);
        this.moduleDependency = moduleDependency;
    }
    public Pom(ModuleLibrary moduleType,
               Path modulePath,
               ConfigFileType configFileType,
               PomDependency moduleDependency,
               ProgramModule parent,
               ProgramModule child) {
        super(moduleType, modulePath, configFileType, parent, child);
        this.moduleDependency = moduleDependency;
    }
}
