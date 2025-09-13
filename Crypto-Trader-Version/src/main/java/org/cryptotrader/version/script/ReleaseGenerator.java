package org.cryptotrader.version.script;

import com.sigwarthsoftware.promo.github.commit.CommitRange;

import com.sigwarthsoftware.changelog.ReleaseChangelogGenerator;
import org.cryptotrader.version.library.model.module.ModuleLibrary;

import java.util.List;

public class ReleaseGenerator {
    public static List<com.sigwarthsoftware.changelog.version.models.module.ModuleLibrary> getAllSigwarthModules() {
        return ModuleLibrary.MODULES.stream().map(ReleaseGenerator::toSigwarthFormat).toList();
    }
    
    public static com.sigwarthsoftware.changelog.version.models.module.ModuleLibrary toSigwarthFormat(ModuleLibrary library) {
        return new com.sigwarthsoftware.changelog.version.models.module.ModuleLibrary(library.getName());
    }
    
    public static void main(String[] args) {
        com.sigwarthsoftware.changelog.version.models.module.ModuleLibrary.initializeModules(getAllSigwarthModules());
        String releaseVersion = "0.2.2";
        CommitRange commitRange = new CommitRange("b64bee7f85e0849a3d8fd91d21cef9533e40bb63", "f3f0332c3f4b8e5001136c1e05dbd40922a1e133");
        String changelog = ReleaseChangelogGenerator.getChangelog(commitRange, releaseVersion);
    }
}
