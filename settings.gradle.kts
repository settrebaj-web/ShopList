pluginManagement {
    repositories { google(); mavenCentral(); gradlePluginPortal() }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories { google(); mavenCentral() }
}
rootProject.name = "ShopList"
include(":app")
// 14-тапсырма: Macrobenchmark үшін келесі жолдың комментін алыңыз
// include(":benchmark")
