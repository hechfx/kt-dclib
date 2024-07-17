plugins {
    kotlin("jvm") version "2.0.0"
}

group = "me.hechfx"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // the library
    implementation(project(":growset"))

    // corou <3 tines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0-RC")

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}