package org.cryptotrader.version;

import com.sigwarthsoftware.changelog.ReleaseChangelogGenerator;
import com.sigwarthsoftware.changelog.version.models.module.ModuleLibrary;
import com.sigwarthsoftware.promo.github.commit.CommitRange;
import org.cryptotrader.version.model.element.PomElement;
import org.cryptotrader.version.model.module.type.Pom;
import org.cryptotrader.version.script.PomParser;
import org.cryptotrader.version.script.PomValidator;

import java.util.List;

@Deprecated(forRemoval = true)
public class Main {
    public static void main(String[] args) {
        List<PomElement> allPoms = PomParser.getAllPoms();
        allPoms.forEach(pom -> {
            System.out.println(pom + "\n");
        });
        System.out.println(PomValidator.cryptoTraderParentMatches());
    }
    
    private static void generateChangelogs(CommitRange commitRange, String version) {
        ModuleLibrary.initializeModules(
                List.of(
                        new ModuleLibrary("Crypto-Trader-Admin"),
                        new ModuleLibrary("Crypto-Trader-Analysis"),
                        new ModuleLibrary("Crypto-Trader-Api"),
                        new ModuleLibrary("Crypto-Trader-Assets"),
                        new ModuleLibrary("Crypto-Trader-Library"),
                        new ModuleLibrary("Api-Library"),
                        new ModuleLibrary("Api-Communication"),
                        new ModuleLibrary("Api-Components"),
                        new ModuleLibrary("Api-Models"),
                        new ModuleLibrary("Api-Repositories"),
                        new ModuleLibrary("Desktop-Library"),
                        new ModuleLibrary("Desktop-Components"),
                        new ModuleLibrary("Version-Library"),
                        new ModuleLibrary("Version-Models"),
                        new ModuleLibrary("Externals-Library"),
                        new ModuleLibrary("Crypto-Trader-Testing"),
                        new ModuleLibrary("Crypto-Trader-Version"),
                        new ModuleLibrary("Crypto-Trader-Website"),
                        new ModuleLibrary("Crypto-Trader")
                )
        );
        String changelog = ReleaseChangelogGenerator.getChangelog(commitRange, version);
        System.out.println(changelog);
    }
}