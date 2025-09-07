plugins {
    id("com.android.application") version "8.6.1"
    kotlin("android") version "2.0.20"
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.20"
}

kotlin { jvmToolchain(21) }

android {
    namespace = "org.cryptotrader.mobile"
    compileSdk = 36

    defaultConfig {
        applicationId = "org.cryptotrader.mobile"
        minSdk = 26
        //noinspection EditedTargetSdkVersion
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures { compose = true }
    composeOptions { kotlinCompilerExtensionVersion = "1.5.15" }


    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    kotlinOptions { jvmTarget = "21" }
}
//noinspection GradleDependency
dependencies {
    implementation(platform("androidx.compose:compose-bom:2024.09.01"))
    implementation("androidx.activity:activity-compose:1.9.2")
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.navigation:navigation-compose:2.8.0")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2024.09.01"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
}
