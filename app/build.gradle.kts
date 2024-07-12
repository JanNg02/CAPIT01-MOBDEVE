plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.s11.ng.jan.capit01_mobdeve"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.s11.ng.jan.capit01_mobdeve"
        minSdk = 21
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
    implementation(libs.play.services.maps)
    implementation ("com.google.android.gms:play-services-maps:17.0.0")
    implementation ("com.google.maps.android:android-maps-utils:2.2.5")
    implementation(libs.volley)
    implementation("com.google.code.gson:gson:2.8.6")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation(libs.play.services.location)
    implementation(libs.firebase.messaging.ktx)
    implementation(libs.androidx.swiperefreshlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    //Mapbox Dependencies
    implementation("com.mapbox.maps:android:11.4.1")

    // If you're using compose also add the compose extension
    implementation("com.mapbox.extension:maps-compose:11.4.1")
}

configurations.all {
    resolutionStrategy {
        force("org.mongodb:bson-record-codec:5.1.1")
    }


}