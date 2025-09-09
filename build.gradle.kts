plugins {
    id("io.gitlab.arturbosch.detekt") version "1.23.6" apply false
    id("com.diffplug.spotless") version "6.25.0" apply false
}

allprojects {
}

subprojects {
    // Apply Spotless to modules with Kotlin
    plugins.withId("org.jetbrains.kotlin.android") {
        apply(plugin = "com.diffplug.spotless")
        configure<com.diffplug.gradle.spotless.SpotlessExtension> {
            kotlin {
                target("**/*.kt")
                ktlint()
            }
            kotlinGradle {
                target("**/*.kts")
                ktlint()
            }
        }
    }
    // Apply Detekt to Kotlin Android modules
    plugins.withId("org.jetbrains.kotlin.android") {
        apply(plugin = "io.gitlab.arturbosch.detekt")
        configure<io.gitlab.arturbosch.detekt.extensions.DetektExtension> {
            buildUponDefaultConfig = true
            allRules = false
        }
        dependencies {
            "detektPlugins"("io.gitlab.arturbosch.detekt:detekt-formatting:1.23.6")
        }
    }
}
