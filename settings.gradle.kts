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
include(":core:core-network")
include(":core:core-database")
include(":core:core-datastore")

include(":feature:feature-profile:domain")
include(":feature:feature-profile:data")
include(":feature:feature-settings:domain")
include(":feature:feature-settings:data")
include(":feature:feature-chats:domain")
include(":feature:feature-chats:data")
include(":feature:feature-chat:domain")
include(":feature:feature-chat:data")
include(":feature:feature-search:domain")
include(":feature:feature-search:data")
include(":feature:feature-recommendations:domain")
include(":feature:feature-recommendations:data")
include(":feature:feature-auth:domain")
include(":feature:feature-auth:data")
