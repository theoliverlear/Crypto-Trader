package org.cryptotrader.version.model.module;

import lombok.*;
import org.cryptotrader.version.model.config.ConfigFileType;

import java.nio.file.Path;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProgramModule {
    private ModuleLibrary moduleType;
    private Path modulePath;
    private ConfigFileType configFileType;
    private String name;
    private String version;

    public ProgramModule(ModuleLibrary moduleType,
                         Path modulePath,
                         ConfigFileType configFileType) {
        this.moduleType = moduleType;
        this.modulePath = modulePath;
        this.configFileType = configFileType;
    }
}
