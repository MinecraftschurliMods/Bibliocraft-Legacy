plugins {
    idea
    id("net.neoforged.gradle.userdev")
    id("at.minecraftschurli.gradle.modhelper")
}

subsystems.conventions.runs.shouldDefaultRunsBeCreated.set(false)

helper {
    withApiSourceSet()
    withDataGenSourceSet()
    withTestSourceSet()

    withClientRun()
    withServerRun()
    withDataGenRuns()

    publication.pom {
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
}

dependencies {
    mods {
        // jei for integration
        jei {
            apiArtifact("mezz.jei:jei-{mc}-neoforge-api:{version}")
        }
    }

    implementation("at.minecraftschurli.mods:dynassetgen:26.1.2-1.0-SNAPSHOT")
    jarJar("at.minecraftschurli.mods:dynassetgen:26.1.2-1.0-SNAPSHOT")

    "modCompileOnly"("org.jetbrains:annotations:26.1.0")
}
