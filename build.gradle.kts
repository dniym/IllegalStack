plugins {
    java
    `java-library`

    idea
    eclipse
}

repositories {
    mavenCentral()
    maven {
        name = "OSS Sonatype"
        url = uri("https://oss.sonatype.org/content/repositories/snapshots/")
    }
    maven {
        name = "Spigot"
        url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
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
    flatDir {
        dirs = setOf(file("libs"), file("$rootDir/libs"))
    }
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.17.1-R0.1-SNAPSHOT")
    compileOnly("com.comphenix.protocol:ProtocolLib:4.7.0-SNAPSHOT")
    implementation("com.elmakers.mine.bukkit:MagicAPI:8.6")
    implementation("de.tr7zw:item-nbt-api-plugin:2.8.0")
    implementation("com.github.TheBusyBiscuit:Slimefun4:v4.1.15") { isTransitive = false }
    implementation("io.netty:netty-all:4.1.65.Final")
    implementation("com.gmail.nossr50.mcMMO:mcMMO:2.1.200") { isTransitive = false }
    implementation("com.github.brcdev-minecraft:shopgui-api:2.2.0")
    implementation("fr.minuskube.inv:smart-invs:1.2.7")
    implementation("com.github.CraftingStore:MinecraftPlugin:master-SNAPSHOT")
    compileOnly(":JetsMinions")
    compileOnlyApi("org.apache.logging.log4j:log4j-api:2.0.1")
    compileOnly("org.jetbrains:annotations:21.0.1")
}

the<JavaPluginExtension>().toolchain {
    languageVersion.set(JavaLanguageVersion.of(16))
}

configurations.all {
    attributes.attribute(TargetJvmVersion.TARGET_JVM_VERSION_ATTRIBUTE, 16)
}

tasks.compileJava.configure {
    options.release.set(8)
}

version = "2.4.2"

tasks.named<Copy>("processResources") {
    filesMatching("plugin.yml") {
        expand("version" to project.version)
    }
}
