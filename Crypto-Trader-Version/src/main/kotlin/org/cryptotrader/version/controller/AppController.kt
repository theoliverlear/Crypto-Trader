package org.cryptotrader.version.controller

import javafx.fxml.FXML
import javafx.scene.layout.BorderPane
import javafx.scene.layout.VBox
import lombok.extern.slf4j.Slf4j
import org.cryptotrader.desktop.library.component.ViewLoader
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Slf4j
@Component
class AppController @Autowired constructor(
    private val viewLoader: ViewLoader
) {

    @FXML
    private val page: VBox? = null

    @FXML
    private val root: BorderPane? = null

    @FXML
    private fun initialize() {
    }

}