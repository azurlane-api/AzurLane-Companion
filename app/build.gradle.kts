import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
}

object Versions {
    private const val versionMajor = 1
    private  const val versionMinor = 7
    private const val versionPatch = 0

    const val minSdk = 24
    const val targetSdk = 29

    fun generateVersionCode(): Int = minSdk * 10000000 + versionMajor * 10000 + versionMinor * 100 + versionPatch

    fun generateVersionName(): String = "${versionMajor}.${versionMinor}.${versionPatch}"
}

android {
    compileSdkVersion(Versions.targetSdk)

    defaultConfig {
        applicationId = "info.kurozeropb.alcompanion"
        minSdkVersion(Versions.minSdk)
        targetSdkVersion(Versions.targetSdk)
        versionCode = Versions.generateVersionCode()
        versionName = Versions.generateVersionName()
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    (kotlinOptions as KotlinJvmOptions).apply {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }

    externalNativeBuild {
        cmake {
            setVersion("3.10.2")
            setPath(file("src/main/cpp/CMakeLists.txt"))
        }
    }

    flavorDimensions("cpuArch")
}

dependencies {
//    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(kotlin("stdlib"))
    implementation("androidx.appcompat:appcompat:1.1.0")
    implementation("androidx.core:core-ktx:1.1.0")
    implementation("androidx.constraintlayout:constraintlayout:1.1.3")
    implementation("androidx.navigation:navigation-fragment:2.1.0")
    implementation("androidx.navigation:navigation-ui:2.1.0")
    implementation("androidx.lifecycle:lifecycle-extensions:2.1.0")
    implementation("androidx.navigation:navigation-fragment-ktx:2.1.0")
    implementation("androidx.navigation:navigation-ui-ktx:2.1.0")
    implementation("com.android.support:customtabs:28.0.0")

    implementation("org.jetbrains.anko:anko:0.10.8")
    implementation("org.jetbrains.anko:anko-design:0.10.8")
    implementation("com.github.kittinunf.fuel:fuel:2.2.1")
    implementation("com.google.code.gson:gson:2.8.6")
    implementation("com.cesards.android:cropimageview:1.0.2.1")
    implementation("com.facebook.fresco:fresco:2.0.0")
    implementation("com.github.stfalcon:frescoimageviewer:0.5.0")
    implementation("com.squareup.picasso:picasso:2.71828")
    implementation("com.hendraanggrian:pikasso:0.2")
    implementation("com.github.bumptech.glide:glide:4.9.0") {
        exclude(group = "com.android.support")
    }
    implementation ("com.github.bumptech.glide:glide:4.9.0@aar") {
        isTransitive = true
    }

    kapt("androidx.annotation:annotation:1.1.0")
    kapt("com.github.bumptech.glide:compiler:4.9.0")

    testImplementation("junit:junit:4.12")
    androidTestImplementation("androidx.test.ext:junit:1.1.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.2.0")
}
