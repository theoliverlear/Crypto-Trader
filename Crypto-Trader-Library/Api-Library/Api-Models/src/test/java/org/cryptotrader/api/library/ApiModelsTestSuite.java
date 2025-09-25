package org.cryptotrader.api.library;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectPackages({"org.cryptotrader.api.library.entity",
                 "org.cryptotrader.api.library.model"})
@DisplayName("API Models Test Suite")
@Tag("suite")
public class ApiModelsTestSuite { }
