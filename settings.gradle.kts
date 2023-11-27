pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Cinema Stream"
include(":app")
include(":app:mylibrary")
include(":app:db")
include(":app:network")
include(":app:screen")
include(":app:mvi")
