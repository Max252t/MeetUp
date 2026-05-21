pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "MeetUp"
include(":app")

include(":core:core-common")

include(":feature:feature-profile:domain")
include(":feature:feature-settings:domain")
include(":feature:feature-chats:domain")
include(":feature:feature-chat:domain")
include(":feature:feature-search:domain")
include(":feature:feature-recommendations:domain")
include(":feature:feature-auth:domain")
