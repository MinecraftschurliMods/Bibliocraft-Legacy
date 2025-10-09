import com.github.minecraftschurlimods.helperplugin.api
import com.github.minecraftschurlimods.helperplugin.version
import org.gradle.api.tasks.compile.JavaCompile

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
        name = "Curse Maven"
        url = uri("https://cursemaven.com")
        content {
            includeGroup("curse.maven")
        }
    }
}

val jei = helper.dependencies.jei()
val abnormalsCompat = true

dependencies {
    implementation(helper.neoforge())
    testImplementation(helper.testframework())

    // jei for integration
    val jeiApiDep = helper.minecraftVersion.zip(jei.version) { mc, version -> "mezz.jei:jei-1.21.9-common-api:${version}" }
    val jeiDep = helper.minecraftVersion.zip(jei.version) { mc, version -> "mezz.jei:jei-1.21.9-neoforge:${version}" }
    compileOnly(jeiApiDep)
    if (!helper.runningInCI.getOrElse(false)) {
        runtimeOnly(jeiDep)
    }

    // abnormals mods for integration
    if (abnormalsCompat) {
        runtimeOnly("curse.maven:blueprint-382216:6449863")
        runtimeOnly("curse.maven:buzzier-bees-355458:6449894")
        "dataRuntimeOnly"("curse.maven:gallery-1173553:6449910")
        "dataRuntimeOnly"("curse.maven:blueprint-382216:6449863")
        "dataRuntimeOnly"("curse.maven:buzzier-bees-355458:6449894")
    }

    testImplementation("org.junit.jupiter:junit-jupiter:${project.properties["junit_version"]}")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    sourceSets.forEach {
        it.compileOnlyConfigurationName("org.jetbrains:annotations:23.0.0")
    }
}

helper.withCommonRuns()
helper.withGameTestRuns()
helper.withDataGenRuns {
    if (abnormalsCompat) {
        arguments("--existing-mod", "buzzier_bees")
    }
}

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
