import java.text.SimpleDateFormat
import java.util.Date

plugins {
    kotlin("jvm")
    id("com.gradleup.shadow")
    id("xyz.jpenilla.run-paper")
}

val buildTime = SimpleDateFormat("yyyyMMdd-HHmmss").format(Date())

dependencies {
    implementation(project(":vocchipet-api"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.14.0")
    compileOnly("io.papermc.paper:paper-api:26.1.2.build.+")
    testImplementation("io.papermc.paper:paper-api:26.1.2.build.+")
    testImplementation("com.github.seeseemelk:MockBukkit-v1.21:3.128.0")
}

tasks {
    shadowJar {
        relocate("com.voc2048.vocchiPet", "com.voc2048.vocchipet")
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
