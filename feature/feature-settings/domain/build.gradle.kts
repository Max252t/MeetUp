plugins {
    alias(libs.plugins.kotlin.jvm)
}

kotlin {
    jvmToolchain(11)
}

dependencies {
    implementation(project(":core:core-common"))
    implementation(libs.kotlinx.coroutines.core)
    compileOnly(libs.javax.inject)
}
