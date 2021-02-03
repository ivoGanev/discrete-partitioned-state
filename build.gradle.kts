import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.10"
    id("org.jetbrains.dokka") version "1.4.20"
    id("java-library")
}

java {
    withSourcesJar()
    // and/or analogously use “withJavadocJar()” to get a “javadocJar” task
}

group = "me.ivgan54"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test-junit"))
}

tasks.test {
    useJUnit()
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "1.8"
}


