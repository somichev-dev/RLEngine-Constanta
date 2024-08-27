plugins {
    kotlin("jvm") version "2.0.10"
    id("xyz.jpenilla.run-paper") version "2.3.1"
    id("com.gradleup.shadow") version "8.3.0"
    id("io.papermc.paperweight.userdev") version "1.7.2"
}

group = "dev.somichev"
version = "0.1.0"
description = "custom RLEngine fork for Constanta SMP"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    paperweight.paperDevBundle("1.20.4-R0.1-SNAPSHOT")
}

tasks.register("printVersionName") {
    println(version)
}

tasks {
    runServer {
        minecraftVersion("1.20.4")
    }
    test {
        useJUnitPlatform()
    }
    // Configure reobfJar to run when invoking the build task
    assemble {
        dependsOn(reobfJar)
    }
    compileJava {
        options.encoding = Charsets.UTF_8.name()
        options.release.set(21)
    }
    javadoc {
        options.encoding = Charsets.UTF_8.name()
    }
    processResources {
        filteringCharset = Charsets.UTF_8.name()
        val props =
            mapOf(
                "version" to project.version,
                "description" to project.description,
                "apiVersion" to "1.20",
            )
        inputs.properties(props)
        filesMatching("plugin.yml") {
            expand(props)
        }
    }
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}
kotlin {
    jvmToolchain(21)
}
