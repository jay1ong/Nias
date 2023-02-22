plugins {
    id("nowinandroid.android.feature")
    id("nowinandroid.android.library.compose")
    id("nowinandroid.android.library.jacoco")
}

android {
    namespace = "cn.jaylong.nias.feature.topic"
}

dependencies {
    implementation(libs.kotlinx.datetime)
}