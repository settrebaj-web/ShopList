plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
    id("org.jlleitschuh.gradle.ktlint")
    id("io.gitlab.arturbosch.detekt")
    id("jacoco")
}

android {
    namespace = "com.example.shop"
    compileSdk = 34
    buildFeatures {
        buildConfig = true
    }

    defaultConfig {
        applicationId = "com.example.shop"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        // 10-тапсырма: әр тесттен кейін деректерді тазалау (Orchestrator)
        testInstrumentationRunnerArguments["clearPackageData"] = "true"
    }

    buildTypes {
        debug {
            enableUnitTestCoverage = true
            enableAndroidTestCoverage = true
        }
        release {
            isMinifyEnabled = false
        }
        // 14-тапсырма: Macrobenchmark үшін
        create("benchmark") {
            initWith(getByName("release"))
            signingConfig = signingConfigs.getByName("debug")
            matchingFallbacks += listOf("release")
            isDebuggable = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions { jvmTarget = "17" }

    testOptions {
        execution = "ANDROIDX_TEST_ORCHESTRATOR"
        animationsDisabled = true
        unitTests.isIncludeAndroidResources = true // Robolectric үшін
    }

    // Room миграция тесттері схема JSON файлдарын assets ретінде оқиды
    sourceSets {
        getByName("androidTest").assets.srcDir("$projectDir/schemas")
    }
}

ksp {
    arg("room.schemaLocation", "$projectDir/schemas")
}

ktlint { ignoreFailures.set(true) }
detekt {
    buildUponDefaultConfig = true
    ignoreFailures = true
}

dependencies {
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("androidx.activity:activity-ktx:1.9.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.4")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.4")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")

    implementation("com.jakewharton.timber:timber:5.0.1")
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    ksp("androidx.room:room-compiler:2.6.1")
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation("androidx.test.espresso:espresso-idling-resource:3.7.0") // ЖАҢА
    debugImplementation("com.squareup.leakcanary:leakcanary-android:2.14")

    // JVM (unit) тесттер
    testImplementation("junit:junit:4.13.2")
    testImplementation("io.mockk:mockk:1.13.12")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.8.1")
    testImplementation("org.robolectric:robolectric:4.13")
    testImplementation("androidx.test:core-ktx:1.6.1")
    testImplementation("androidx.test.ext:junit:1.2.1")
    testImplementation("com.squareup.okhttp3:mockwebserver:4.12.0")

    // Instrumented тесттер
    androidTestImplementation("androidx.test:runner:1.7.0") // ЖАҢА
    androidTestImplementation("androidx.test:rules:1.7.0") // ЖАҢА
    androidTestImplementation("androidx.test.ext:junit:1.3.0") // ЖАҢА
    androidTestImplementation("androidx.test.espresso:espresso-core:3.7.0") // ЖАҢА
    androidTestImplementation("androidx.test.espresso:espresso-contrib:3.7.0") // ЖАҢА
    androidTestImplementation("androidx.test.espresso:espresso-intents:3.7.0") // ЖАҢА
    androidTestImplementation("androidx.room:room-testing:2.6.1")
    androidTestImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.8.1")
    androidTestUtil("androidx.test:orchestrator:1.5.0")
}

// 12-тапсырма: JaCoCo есебі (unit тесттер)
tasks.register<JacocoReport>("jacocoReport") {
    dependsOn("testDebugUnitTest")
    reports {
        html.required.set(true)
        xml.required.set(true)
    }
    val buildDirPath = layout.buildDirectory.get().asFile.path
    val excludes = listOf(
        "**/R.class", "**/R$*.class", "**/BuildConfig.*",
        "**/*_Impl*", "**/databinding/**"
    )
    classDirectories.setFrom(
        fileTree("$buildDirPath/tmp/kotlin-classes/debug") { exclude(excludes) }
    )
    sourceDirectories.setFrom("src/main/java")
    executionData.setFrom(
        fileTree(buildDirPath) { include("**/*.exec", "**/*.ec") }
    )
}