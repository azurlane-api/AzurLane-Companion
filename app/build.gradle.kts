plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
}

object Versions {
    private const val versionMajor = 1
    private  const val versionMinor = 0
    private const val versionPatch = 0

    const val minSdk = 24
    const val targetSdk = 29

    fun generateVersionCode(): Int = minSdk * 10000000 + versionMajor * 10000 + versionMinor * 100 + versionPatch

    fun generateVersionName(): String = "${versionMajor}.${versionMinor}.${versionPatch}"
}

android {
    compileSdkVersion(Versions.targetSdk)

    defaultConfig {
        applicationId = "info.kurozeropb.azurlane"
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
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.3.60-eap-25")
    implementation("androidx.appcompat:appcompat:1.1.0")
    implementation("androidx.core:core-ktx:1.1.0")
    implementation("androidx.constraintlayout:constraintlayout:1.1.3")
    implementation("org.jetbrains.anko:anko:0.10.8")
    implementation("org.jetbrains.anko:anko-design:0.10.8")
    implementation("com.github.kittinunf.fuel:fuel:2.2.1")
    implementation("com.google.code.gson:gson:2.8.6")
    implementation("com.cesards.android:cropimageview:1.0.2.1")
    implementation("com.facebook.fresco:fresco:1.9.0")
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
