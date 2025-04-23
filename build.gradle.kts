import com.github.minecraftschurlimods.helperplugin.api
import com.github.minecraftschurlimods.helperplugin.version

plugins {
    idea
    id("net.neoforged.gradle.userdev")
    id("com.github.minecraftschurlimods.helperplugin")
}

helper.withApiSourceSet()
helper.withDataGenSourceSet()
helper.withTestSourceSet()

repositories {
    mavenLocal()
    mavenCentral()
    maven {
        name = "blamejared Maven"
        url = uri("https://maven.blamejared.com")
        content {
            includeGroup("mezz.jei")
        }
    }
    maven {
        name = "Abnormals Maven"
        url = uri("https://maven.jaackson.me")
        content {
            includeGroup("com.teamabnormals")
        }
    }
}

val jei = helper.dependencies.jei()

dependencies {
    implementation(helper.neoforge())
    testImplementation(helper.testframework())

    // jei for integration
    val jeiApiDep = helper.minecraftVersion.zip(jei.version) { mc, version -> "mezz.jei:jei-${mc}-common-api:${version}" }
    val jeiDep = helper.minecraftVersion.zip(jei.version) { mc, version -> "mezz.jei:jei-${mc}-neoforge:${version}" }
    compileOnly(jeiApiDep)
    if (!helper.runningInCI.getOrElse(false)) {
        runtimeOnly(jeiDep)
    }

    // abnormals mods for integration
    if (!helper.runningInCI.getOrElse(false)) {
        runtimeOnly("com.teamabnormals:blueprint:${helper.minecraftVersion.get()}-${project.properties["blueprint_version"]}")
    }
    if (!helper.runningInCI.getOrElse(false)) {
        runtimeOnly("com.teamabnormals:gallery:${helper.minecraftVersion.get()}-${project.properties["gallery_version"]}")
    }

    testImplementation("org.junit.jupiter:junit-jupiter:${project.properties["junit_version"]}")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    sourceSets.forEach {
        it.compileOnlyConfigurationName("org.jetbrains:annotations:23.0.0")
    }
}

helper.withCommonRuns()
helper.withGameTestRuns()
helper.withDataGenRuns()

minecraft.accessTransformers.file("src/main/resources/META-INF/accesstransformer.cfg")

tasks.javadoc {
    classpath = sourceSets.api.get().compileClasspath
    source = sourceSets.api.get().allJava
}

helper.publication.pom {
    organization {
        name = "Minecraftschurli Mods"
        url = "https://github.com/MinecraftschurliMods"
    }
    developers {
        developer {
            id = "minecraftschurli"
            name = "Minecraftschurli"
            email = "minecraftschurli@gmail.com"
            url = "https://github.com/Minecraftschurli"
            organization = "Minecraftschurli Mods"
            organizationUrl = "https://github.com/MinecraftschurliMods"
            timezone = "Europe/Vienna"
        }
        developer {
            id = "ichhabehunger54"
            name = "IchHabeHunger54"
            url = "https://github.com/IchHabeHunger54"
            organization = "Minecraftschurli Mods"
            organizationUrl = "https://github.com/MinecraftschurliMods"
            timezone = "Europe/Vienna"
        }
    }
}
