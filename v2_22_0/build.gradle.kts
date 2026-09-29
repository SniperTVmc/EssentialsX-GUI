plugins {
    id("java-library")
}

java.sourceCompatibility = JavaVersion.VERSION_21

repositories {

    // PaperMC
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

    compileOnly("io.papermc.paper:paper-api:1.21.11-R0.1-SNAPSHOT")
    compileOnly("net.essentialsx:EssentialsX:2.22.0-SNAPSHOT") {
        exclude(group = "io.papermc.paper", module = "paper-api")
    }
}
