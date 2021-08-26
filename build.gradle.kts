import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.10"
    application
}

group = "com.valllent"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(files("src/libs/fernflower.jar"))
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "1.8"
}

application {
    mainClass.set("MainKt")
}