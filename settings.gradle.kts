pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/revanced/registry")
            credentials {
                username = providers.gradleProperty("gpr.user").orNull ?: System.getenv("GITHUB_ACTOR")
                password = providers.gradleProperty("gpr.key").orNull ?: System.getenv("GITHUB_TOKEN")
            }
        }
    }
}

plugins {
    id("app.revanced.patches") version "1.3.0"
}

settings {
    patchesProjectPath = "patches"
    extensions {
        projectsPath = "extensions"
        defaultNamespace = "app.revanced.extension"
    }
}

rootProject.name = "rhyan57-patches"
