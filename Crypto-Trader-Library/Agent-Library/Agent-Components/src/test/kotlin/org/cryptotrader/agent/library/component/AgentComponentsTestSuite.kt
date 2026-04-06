package org.cryptotrader.agent.library.component

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Tag
import org.junit.platform.suite.api.SelectPackages
import org.junit.platform.suite.api.Suite

@Suite
@SelectPackages("org.cryptotrader.agent.library.component")
@DisplayName("Agent Components Test Suite")
@Tag("suite")
class AgentComponentsTestSuite