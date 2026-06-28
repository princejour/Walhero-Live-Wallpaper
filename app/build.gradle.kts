plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.walhero.livewallpaper"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.walhero.livewallpaper"
        minSdk = 23
        targetSdk = 34
        versionCode = 3
        versionName = "1.0.2"
    }

    signingConfigs {
        create("walheroRelease") {
            storeFile = file(System.getenv("SIGNING_STORE_FILE"))
            storePassword = System.getenv("SIGNING_STORE_PASSWORD")
            keyAlias = System.getenv("SIGNING_KEY_ALIAS")
            keyPassword = System.getenv("SIGNING_KEY_PASSWORD")
        }
    }

    buildTypes {
        debug {
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-debug"
        }
        release {
            isDebuggable = false
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName("walheroRelease")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}

kotlin {
    jvmToolchain(17)
}
