pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven(url = "https://jitpack.io")
    }
}

rootProject.name = "tachiyomi-extension"

include(":core")
include(":src:all:tachidesk")
include(":src:th:bullymanga")
include(":src:th:nanomanga")
include(":src:th:pedmanga")
include(":src:th:mangalc")
