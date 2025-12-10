package org.cryptotrader.api.library.config

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Tag
import org.junit.platform.suite.api.SelectPackages
import org.junit.platform.suite.api.Suite

@Suite
@SelectPackages("org.cryptotrader.api.library.config")
@DisplayName("Api Config Test Suite")
@Tag("suite")
class ApiConfigTestSuite