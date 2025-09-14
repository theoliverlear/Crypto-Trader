package org.cryptotrader.version

import fr.brouillard.oss.cssfx.CSSFX
import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyCodeCombination
import javafx.scene.input.KeyCombination
import javafx.stage.Stage
import javafx.util.Callback
import org.cryptotrader.assets.images.logos.cryptotrader.fx.CryptoTraderLogoImageAssets
import org.cryptotrader.version.config.SpringBootConfig
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.context.ConfigurableApplicationContext
import java.io.IOException

class VersionApplication : Application() {
    private lateinit var applicationContext: ConfigurableApplicationContext

    override fun init() {
        System.setProperty("spring.aop.proxy-target-class", "false")
        System.setProperty("spring.http.client.factory", "JDK")
        this.applicationContext = SpringApplicationBuilder(SpringBootConfig::class.java)
            .headless(false)
            .run()
    }

    override fun start(stage: Stage) {
        val fxmlLoader = FXMLLoader(VersionApplication::class.java.getResource("ui/view/app/AppView.fxml"))
        fxmlLoader.controllerFactory = Callback { requiredType: Class<*>? ->
            this.applicationContext.getBean(
                requiredType
            )
        }
        val scene = Scene(fxmlLoader.load(), 800.0, 600.0)
        stage.title = "Crypto Trader Version Panel"
        val cssPath =
            VersionApplication::class.java.getResource("ui/view/app/AppView.css")
        if (cssPath != null) {
            scene.stylesheets.add(cssPath.toExternalForm())
        }
        stage.setScene(scene)
        CSSFX.start()
        stage.show()

        //        ScenicView.show(scene);
        stage.getIcons()
            .add(CryptoTraderLogoImageAssets.CROPPED_TRANSPARENT_PNG)
        this.addReloadKeybind(scene)
    }

    private fun addReloadKeybind(scene: Scene) {
        scene.accelerators[KeyCodeCombination(
            KeyCode.R,
            KeyCombination.CONTROL_DOWN,
            KeyCombination.SHIFT_DOWN
        )] = Runnable {
            reloadScene(scene)
        }
    }


    fun reloadScene(scene: Scene) {
        try {
            val reloadLoader = FXMLLoader(
                VersionApplication::class.java.getResource("ui/view/app/AppView.fxml")
            )
            reloadLoader.controllerFactory = Callback { requiredType: Class<*>? ->
                this.applicationContext.getBean(
                    requiredType
                )
            }

            val newRoot = reloadLoader.load<Parent?>()
            scene.root = newRoot
        } catch (ex: IOException) {
            ex.printStackTrace()
        }
    }

    override fun stop() {
        this.applicationContext.close()
    }
}