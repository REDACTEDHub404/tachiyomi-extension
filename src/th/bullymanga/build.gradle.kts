ext {
    set("extName", "Bully-Manga")
    set("extClass", ".BullyManga")
    set("themePkg", "mangathemesia")
    set("baseUrl", "https://bully-manga.com")
    set("overrideVersionCode", 1)
    set("isNsfw", false)
}

apply(from = "$rootDir/common.gradle.kts")
