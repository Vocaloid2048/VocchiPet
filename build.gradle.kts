plugins {
    kotlin("jvm") version "2.4.0"
    id("com.gradleup.shadow") version "9.4.3" apply false
    id("xyz.jpenilla.run-paper") version "3.0.2" apply false
}

allprojects {
    group = "com.voc2048"
    version = "1.0.0-SNAPSHOT"

    repositories {
        mavenCentral()
        maven("https://repo.papermc.io/repository/maven-public/")
    }
}

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")

    kotlin {
        jvmToolchain(25)
    }

    dependencies {
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    }
}

// 橋接任務，讓根專案的 runServer 指向 vocchipet-core
tasks.register("runServer") {
    group = "run-paper"
    description = "Run the Paper server (delegated to :vocchipet-core)"
    dependsOn(":vocchipet-core:runServer")
}
