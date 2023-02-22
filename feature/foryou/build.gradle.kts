plugins {
    id("nowinandroid.android.feature")
    id("nowinandroid.android.library.compose")
    id("nowinandroid.android.library.jacoco")
}

android {
    namespace = "cn.jaylong.nias.feature.foryou"
}

dependencies {
    implementation(libs.kotlinx.datetime)

    implementation(libs.accompanist.flowlayout)
}