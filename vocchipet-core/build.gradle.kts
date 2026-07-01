import java.text.SimpleDateFormat
import java.util.Date

plugins {
    kotlin("jvm")
    id("com.gradleup.shadow")
    id("xyz.jpenilla.run-paper")
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

    runServer {
        minecraftVersion("26.1.2")
        jvmArgs("-Xms2G", "-Xmx2G")
        // run-paper 在子模組中會自動抓取該模組的 jar (或 shadowJar)
    }

    processResources {
        val props = mapOf("version" to project.version)
        filesMatching("plugin.yml") {
            expand(props)
        }
    }
}
