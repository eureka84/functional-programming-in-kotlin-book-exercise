group = "org.example"
version = "1.0-SNAPSHOT"
description = "functional-programming-in-kotlin-book-exercises"
java.sourceCompatibility = JavaVersion.VERSION_1_8

plugins {
    java
    id("org.jetbrains.kotlin.jvm") version "1.6.0"
    idea
}

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    implementation("io.arrow-kt:arrow-core:0.11.0")
    implementation("io.arrow-kt:arrow-core-data:0.11.0")
    implementation("io.arrow-kt:arrow-mtl:0.11.0")
    implementation("io.arrow-kt:arrow-mtl-data:0.11.0")
    implementation("io.arrow-kt:arrow-syntax:0.11.0")
    implementation("io.arrow-kt:arrow-fx:0.11.0")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.2")
    testImplementation("io.kotest:kotest-runner-junit5-jvm:5.1.0")
    testImplementation("io.kotest:kotest-assertions-core-jvm:5.1.0")
    testImplementation("io.kotest:kotest-property-jvm:5.1.0")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.8.2")
}

tasks.withType<JavaCompile>() {
    options.encoding = "UTF-8"
}

tasks.test {
    useJUnitPlatform()

    testLogging {
        events("passed", "skipped", "failed")
    }

    reports {
        html.required.set(true)
    }
}


tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}
