plugins {
    id("java")
    id("com.gradleup.shadow") version "9.3.1"
    id("xyz.jpenilla.run-paper") version("3.0.2")
    id("maven-publish")
    id("java-library")
}


group = "fr.snipertvmc.essentialsxgui"
version = "1.4.2"
description = "EssentialsX-GUI"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenLocal()
    mavenCentral()
    maven {
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }

    maven {
        url = uri("https://jitpack.io/")
    }

    maven {
        url = uri("https://repo.essentialsx.net/releases/")
    }

    maven {
        name = "mvn-wesjd-net"
        url = uri("https://mvn.wesjd.net/")
    }

    maven {
        url = uri("https://repo.alessiodp.com/releases/")
    }

    maven {
        url = uri("https://repo.helpch.at/releases/")
    }

    maven {
        name = "faststatsReleases"
        url = uri("https://repo.faststats.dev/releases")
    }

    maven {
        url = uri("https://repo.marcely.de/repository/maven-public/")
    }

    maven {
        url = uri("https://repo.maven.apache.org/maven2/")
    }
}

dependencies {
        compileOnly("org.spigotmc:spigot-api:1.21.1-R0.1-SNAPSHOT")
        compileOnly("net.essentialsx:EssentialsX:2.21.2")
        compileOnly("com.squareup.moshi:moshi:1.15.2")
        compileOnly("com.zaxxer:HikariCP:7.0.2")
        compileOnly("com.github.InstantlyMoist:privatebin-java-api:master")
        compileOnly("me.clip:placeholderapi:2.12.2")
        compileOnly("com.github.cryptomorin:XSeries-Fork:13.6.0")
        implementation("net.wesjd:anvilgui:1.10.11-SNAPSHOT")
        implementation("net.byteflux:libby-bukkit:1.3.1")
        implementation("dev.faststats.metrics:bukkit:0.18.1")
}


publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.withType<Javadoc> {
    options.encoding = "UTF-8"
}

tasks.shadowJar {
    archiveClassifier.set("")

    relocate("net.byteflux.libby", "fr.snipertvmc.essentialsxgui.libraries.libby")
    relocate("net.wesjd.anvilgui", "fr.snipertvmc.essentialsxgui.libraries.anvilgui")
    relocate("net.kyori.adventure", "fr.snipertvmc.essentialsxgui.libraries.adventure")
    relocate("dev.faststats", "fr.snipertvmc.essentialsxgui.libraries.faststats")

    mergeServiceFiles()

    exclude("META-INF/*.SF", "META-INF/*.DSA", "META-INF/*.RSA")
}

tasks.named("build") {
    dependsOn(tasks.named("shadowJar"))
    finalizedBy(tasks.named("copyJar"))
}

tasks {
    runServer {
        downloadPlugins {
            url("https://github.com/EssentialsX/Essentials/releases/download/2.21.2/EssentialsX-2.21.2.jar")
        }
        // Configure the Minecraft version for our task.
        // This is the only required configuration besides applying the plugin.
        // Your plugin"s jar (or shadowJar if present) will be used automatically.
        minecraftVersion("1.21.11")
    }
    runPaper.folia.registerTask()
}
tasks.build {
    dependsOn(tasks.shadowJar)
}

tasks.build {
    finalizedBy(tasks.named("copyJar"))
}

tasks.register<Copy>("copyJar") {
    from(tasks.named("shadowJar"))
    into(layout.buildDirectory.dir("libs"))
}

tasks.processResources {
    val props = mapOf("version" to project.version)
    inputs.properties(props)
    filteringCharset = "UTF-8"

    filesMatching("plugin.yml") {
        expand(props)
    }
}