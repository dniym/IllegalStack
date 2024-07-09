plugins {
    java
    `java-library`

    idea
    eclipse

    id("com.github.johnrengelman.shadow") version "7.1.0"
}

repositories {
    mavenCentral()
    maven {
        name = "OSS Sonatype"
        url = uri("https://oss.sonatype.org/content/repositories/snapshots/")
    }
    maven {
        name = "PaperMC"
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
    maven {
        name = "PaperMC"
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
    maven {
        name = "PacketEvents"
        url = uri("https://repo.codemc.io/repository/maven-releases/")
    }
    maven {
        name = "ProtocolLib"
        url = uri("https://repo.dmulloy2.net/nexus/repository/public/")
    }
    maven {
        name = "Magic"
        url = uri("https://maven.elmakers.com/repository/")
    }
    maven {
        name = "CodeMC"
        url = uri("https://repo.codemc.org/repository/maven-public/")
    }
    maven {
        name = "JitPack"
        url = uri("https://jitpack.io")
    }
}

dependencies {
    compileOnly("dev.folia:folia-api:1.20.4-R0.1-SNAPSHOT")
    compileOnly("com.comphenix.protocol:ProtocolLib:5.0.0")
    compileOnly("com.elmakers.mine.bukkit:MagicAPI:10.2")
    compileOnly("de.tr7zw:item-nbt-api-plugin:2.8.0")
    compileOnly("com.github.TheBusyBiscuit:Slimefun4:RC-30") { isTransitive = false }
    compileOnly("io.netty:netty-all:4.1.110.Final") {
        because("The version aligns with the version used by Minecraft itself." +
                "The minecraft server ships netty as well, so we don't need to include it in the jar.")
    }
    compileOnly("com.gmail.nossr50.mcMMO:mcMMO:2.1.217") { isTransitive = false }
    compileOnly("fr.minuskube.inv:smart-invs:1.2.7")
    //compileOnly("com.github.CraftingStore.MinecraftPlugin:core:master-e366d322f8-1")
    compileOnly("com.github.brcdev-minecraft:shopgui-api:3.0.0")
    implementation("com.github.retrooper:packetevents-spigot:2.4.0")
}

the<JavaPluginExtension>().toolchain {
    languageVersion.set(JavaLanguageVersion.of(17))
}

configurations.all {
    attributes.attribute(TargetJvmVersion.TARGET_JVM_VERSION_ATTRIBUTE, 17)
}

tasks.compileJava.configure {
    options.release.set(8)
}

version = "2.10.0-SNAPSHOT-01"

tasks.named<Copy>("processResources") {
    filesMatching("plugin.yml") {
        expand("version" to project.version)
    }
}

// ShadowJar configuration
tasks.shadowJar {
    // Optionally, relocate the PacketEvents package to avoid conflicts
    relocate("com.github.retrooper.packetevents", "me.dniym.packetevents.api")
    relocate("io.github.retrooper.packetevents", "me.dniym.packetevents.impl")
    minimize()
}

// Build the ShadowJar
tasks.build {
    dependsOn(tasks.shadowJar)
}
