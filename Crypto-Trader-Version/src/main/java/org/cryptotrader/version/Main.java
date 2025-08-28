package org.cryptotrader.version;

import org.cryptotrader.version.model.Module;
import org.cryptotrader.version.script.PomParser;

import java.util.List;

@Deprecated(forRemoval = true)
public class Main {
    public static void main(String[] args) {
        List<Module> allPoms = PomParser.getAllPoms();
        allPoms.forEach(System.out::println);
    }
}