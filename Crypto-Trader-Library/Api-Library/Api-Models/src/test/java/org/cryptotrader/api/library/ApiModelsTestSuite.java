package org.cryptotrader.api.library;

import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectPackages({"org.cryptotrader.api.library.entity",
                 "org.cryptotrader.api.library.model"})
public class ApiModelsTestSuite { }
