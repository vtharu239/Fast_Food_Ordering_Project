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
        //help for banner
        maven {
            setUrl("https://jitpack.io")
        }
    }
}

rootProject.name = "Project_Food"
include(":app")
