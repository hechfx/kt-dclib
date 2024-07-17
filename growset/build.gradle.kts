plugins {
    kotlin("jvm")
    kotlin("plugin.serialization") version "2.0.0"
}

group = "me.hechfx"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    val ktx_serialization = "1.7.1"
    val ktor_version = "2.3.12"
    val caffeine_version = "3.1.8"
    val kotlin_logging_version = "5.1.4"

    // Serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$ktx_serialization")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor_version")

    // Ktor
    implementation("io.ktor:ktor-client-core:$ktor_version")
    implementation("io.ktor:ktor-client-cio:$ktor_version")
    implementation("io.ktor:ktor-client-websockets:$ktor_version")
    implementation("io.ktor:ktor-client-content-negotiation:$ktor_version")

    // Caching
    implementation("com.github.ben-manes.caffeine:caffeine:$caffeine_version")

    // Logging
    implementation("io.github.oshai:kotlin-logging-jvm:$kotlin_logging_version")
    implementation("org.slf4j:slf4j-simple:2.0.3")

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}