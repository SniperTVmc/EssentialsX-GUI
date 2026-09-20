plugins {
    id("java-library")
}

java.sourceCompatibility = JavaVersion.VERSION_21

repositories {

    // SpigotAPI
    maven {
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }

    // EssentialsX
    maven {
        url = uri("https://repo.essentialsx.net/snapshots/")
    }
}

dependencies {
    implementation(project(":base"))

    compileOnly("org.spigotmc:spigot-api:26.2-R0.1-SNAPSHOT")
    compileOnly("net.essentialsx:EssentialsX:2.22.0-SNAPSHOT") {
        exclude(group = "io.papermc.paper", module = "paper-api")
    }
}
