import java.text.SimpleDateFormat
import java.util.Date

plugins {
    kotlin("jvm")
    id("com.gradleup.shadow")
}

val buildTime = SimpleDateFormat("yyyyMMdd-HHmm").format(Date())

dependencies {
    implementation(project(":vocchipet-api"))
    compileOnly("io.papermc.paper:paper-api:26.1.2.build.+")
}

tasks {
    shadowJar {
        archiveFileName.set("VocchiPet-${project.version}-$buildTime.jar")
    }

    processResources {
        val props = mapOf("version" to project.version)
        filesMatching("plugin.yml") {
            expand(props)
        }
    }
}
