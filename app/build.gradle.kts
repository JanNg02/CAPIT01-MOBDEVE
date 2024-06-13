plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {
    namespace = "com.example.s11.ng.jan.capit01_mobdeve"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.s11.ng.jan.capit01_mobdeve"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        multiDexEnabled=true

    }



    packaging {
        resources.excludes.add("META-INF/native-image/org.mongodb/bson/native-image.properties")
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation("dnsjava:dnsjava:3.5.0")
//    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
 //   implementation("org.mongodb:mongodb-driver-kotlin-coroutine:4.10.1")
    implementation("androidx.recyclerview:recyclerview:1.2.1")
    implementation(libs.mongodb.driver.kotlin.coroutine){
        exclude("org.mongodb","bson-record-codec")
    }
    implementation(libs.bson.kotlinx){
        exclude("org.mongodb","bson-record-codec")
    }
    implementation(libs.mongodb.mongodb.driver.sync){
        exclude("org.mongodb","bson-record-codec")
    }
     //implementation(libs.bson.record.codec)
    implementation(libs.androidx.multidex)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}

configurations.all {
    resolutionStrategy {
        force("org.mongodb:bson-record-codec:5.1.1")
    }


}