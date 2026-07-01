plugins {
    kotlin("jvm")
    id("com.gradleup.shadow")
}

dependencies {
    implementation(project(":vocchipet-api"))
    compileOnly("io.papermc.paper:paper-api:26.1.2.build.+")
}

tasks {
    shadowJar {
        archiveClassifier.set("")
    }

    processResources {
        val props = mapOf("version" to project.version)
        filesMatching("plugin.yml") {
            expand(props)
        }
    }
}
