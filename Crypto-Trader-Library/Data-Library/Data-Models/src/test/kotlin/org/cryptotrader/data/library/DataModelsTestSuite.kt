package org.cryptotrader.data.library

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Tag
import org.junit.platform.suite.api.SelectPackages
import org.junit.platform.suite.api.Suite

@Suite
@SelectPackages("org.cryptotrader.data.library.entity",
    "org.cryptotrader.data.library.model")
@DisplayName("Data Models Test Suite")
@Tag("suite")
class DataModelsTestSuite