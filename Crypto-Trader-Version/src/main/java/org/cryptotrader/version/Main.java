package org.cryptotrader.version;

import org.cryptotrader.version.model.module.type.Pom;
import org.cryptotrader.version.script.PomParser;

import java.util.List;

@Deprecated(forRemoval = true)
public class Main {
    public static void main(String[] args) {
        List<Pom> allPoms = PomParser.getAllPoms();
        allPoms.forEach(System.out::println);
    }
}