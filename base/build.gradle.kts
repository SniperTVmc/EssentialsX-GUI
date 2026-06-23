import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar.Companion.shadowJar

plugins {
    id("java")
    id("java-library")
    id("com.gradleup.shadow") version "9.4.1"
    id("xyz.jpenilla.run-paper") version "3.0.2"
    id("maven-publish")
}


group = "fr.snipertvmc.essentialsxgui"
version = "1.5.0"
description = "EssentialsX-GUI"
java.sourceCompatibility = JavaVersion.VERSION_21

repositories {
    mavenLocal()

    // Adventure
    mavenCentral()

    // SpigotMC, Moshi, HikariCP, XSeries (v13.6.0+26.1)
    maven {
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }

    // EssentialsX
    maven {
        url = uri("https://repo.essentialsx.net/snapshots/")
    }

    // PrivateBin Java API
    maven {
        url = uri("https://jitpack.io/")
    }

    // PlaceholderAPI
    maven {
        url = uri("https://repo.extendedclip.com/releases/")
    }

    // AnvilGUI
    maven {
        url = uri("https://mvn.wesjd.net/")
    }

    // FastStats
    maven {
        url = uri("https://repo.faststats.dev/releases")
    }
}

dependencies {
    runtimeOnly(project(":v2_21_2"))
    runtimeOnly(project(":v2_22_0"))

    compileOnly("org.spigotmc:spigot-api:1.21.11-R0.1-SNAPSHOT")
    compileOnly("net.essentialsx:EssentialsX:2.21.2-SNAPSHOT") {
        exclude(group = "io.papermc.paper", module = "paper-api")
    }
    compileOnly("com.squareup.moshi:moshi:1.15.2")
    compileOnly("com.zaxxer:HikariCP:7.0.2")
    compileOnly("com.github.InstantlyMoist:privatebin-java-api:master")
    compileOnly("me.clip:placeholderapi:2.12.2")
    compileOnly("io.github.almighty-satan:XSeries:13.6.0+26.1")
    compileOnly("net.kyori:adventure-platform-bukkit:4.4.1")
    compileOnly("net.kyori:adventure-text-minimessage:4.25.0")

    implementation("net.wesjd:anvilgui:1.10.13-SNAPSHOT")
    implementation("dev.faststats.metrics:bukkit:0.22.0")
}


publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
}

tasks.withType<JavaCompile> { options.encoding = "UTF-8" }
tasks.withType<Javadoc> { options.encoding = "UTF-8" }

tasks.jar {
    enabled = false
}

tasks.shadowJar {
    archiveFileName.set("EssentialsX-GUI-${project.version}.jar")
    destinationDirectory.set(layout.buildDirectory.dir("shadow"))

    relocate("net.wesjd.anvilgui", "fr.snipertvmc.essentialsxgui.libraries.anvilgui")
    relocate("dev.faststats", "fr.snipertvmc.essentialsxgui.libraries.faststats")

    mergeServiceFiles()
    exclude("META-INF/*.SF", "META-INF/*.DSA", "META-INF/*.RSA")
}

val copyJar = tasks.register<Copy>("copyJar") {
    description = "Copy the generated EssentialsX-GUI JAR to the root build/libs directory"
    from(tasks.shadowJar.flatMap { it.archiveFile })
    into(rootProject.layout.projectDirectory.dir("build/libs"))
}

tasks {
    runServer {
        downloadPlugins {
            url("https://github.com/EssentialsX/Essentials/releases/download/2.21.2/EssentialsX-2.21.2.jar")
        }
        minecraftVersion("1.21.11")
    }

    runPaper.folia.registerTask()

    build {
        dependsOn(shadowJar)
        finalizedBy(copyJar)
    }
}

tasks.processResources {
    val props = mapOf("version" to project.version)
    inputs.properties(props)
    filteringCharset = "UTF-8"

    filesMatching("plugin.yml") {
        expand(props)
    }
}
