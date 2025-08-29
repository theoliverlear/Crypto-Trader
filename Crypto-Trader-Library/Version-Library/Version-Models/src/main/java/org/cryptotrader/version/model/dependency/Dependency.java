package org.cryptotrader.version.model.dependency;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Getter
public class Dependency {
    private String name;
    private String version;
    
    public Dependency(String name, String version) {
        this.name = name;
        this.version = version;
    }
}
