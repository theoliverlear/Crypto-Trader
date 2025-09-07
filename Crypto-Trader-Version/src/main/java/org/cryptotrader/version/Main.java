package org.cryptotrader.version;

import org.cryptotrader.version.model.element.PomElement;
import org.cryptotrader.version.script.PomParser;
import org.cryptotrader.version.script.PomValidator;

import java.util.List;

@Deprecated(forRemoval = true)
public class Main {
    public static void main(String[] args) {
        List<PomElement> allPoms = PomParser.getAllPoms();
        allPoms.forEach(pomElement -> {
            System.out.println(pomElement.getPom().getModuleDependency().getName());
        });
        System.out.println("All match: " + PomValidator.allMatch());
    }
}