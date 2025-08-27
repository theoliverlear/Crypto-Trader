package org.cryptotrader;

import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectPackages({"org.cryptotrader.entity",
                 "org.cryptotrader.model"})
public class ApiModelsTestSuite {
    
}
