package eu.kanade.tachiyomi.extension.th.bullymanga

import eu.kanade.tachiyomi.multisrc.madara.Madara
import eu.kanade.tachiyomi.network.GET
import eu.kanade.tachiyomi.source.model.Filter
import eu.kanade.tachiyomi.source.model.FilterList
import eu.kanade.tachiyomi.source.model.MangasPage
import eu.kanade.tachiyomi.source.model.Page
import eu.kanade.tachiyomi.source.model.SChapter
import eu.kanade.tachiyomi.source.model.SManga
import okhttp3.Request
import okhttp3.Response
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import rx.Observable

class BullyManga : Madara(
    "Bully Manga",
    "https://bully-manga.com",
    "th",
) {
    override val id: Long = 3232385017393458L

    // Popular Manga
    override fun popularMangaRequest(page: Int): Request {
        return GET("$baseUrl/?page=$page", headers)
    }

    override fun popularMangaParse(response: Response): MangasPage {
        val document = response.asJsoup()
        val mangas = document.select("div.grid div.relative.group").map { element ->
            mangaFromElement(element)
        }
        val hasNextPage = document.selectFirst("a.next") != null
        return MangasPage(mangas, hasNextPage)
    }

    // Latest Updates
    override fun latestUpdatesRequest(page: Int): Request {
        return GET("$baseUrl/?page=$page", headers)
    }

    override fun latestUpdatesParse(response: Response): MangasPage {
        return popularMangaParse(response)
    }

    // Search
    override fun searchMangaRequest(page: Int, query: String, filters: FilterList): Request {
        return GET("$baseUrl/?search=$query&page=$page", headers)
    }

    override fun searchMangaParse(response: Response): MangasPage {
        return popularMangaParse(response)
    }

    // Manga Details
    override fun mangaDetailsRequest(manga: SManga): Request {
        return GET(baseUrl + manga.url, headers)
    }

    override fun mangaDetailsParse(response: Response): SManga {
        val document = response.asJsoup()
        val manga = SManga.create()
        manga.title = document.selectFirst("h1")?.text() ?: ""
        manga.author = document.selectFirst("div:contains(Author) span")?.text() ?: ""
        manga.artist = ""
        manga.description = document.selectFirst("div.description")?.text() ?: ""
        manga.thumbnail_url = document.selectFirst("img.cover")?.attr("src") ?: ""
        manga.status = when {
            document.text().contains("Ongoing") -> SManga.ONGOING
            document.text().contains("Completed") -> SManga.COMPLETED
            else -> SManga.UNKNOWN
        }
        return manga
    }

    // Chapters
    override fun chapterListRequest(manga: SManga): Request {
        return GET(baseUrl + manga.url, headers)
    }

    override fun chapterListParse(response: Response): List<SChapter> {
        val document = response.asJsoup()
        return document.select("div.grid.grid-cols-1 a").map { element ->
            SChapter.create().apply {
                name = element.selectFirst("span.font-medium")?.text() ?: "Chapter"
                url = element.attr("href")
                date_upload = 0
            }
        }.reversed()
    }

    // Pages
    override fun pageListRequest(chapter: SChapter): Request {
        return GET(baseUrl + chapter.url, headers)
    }

    override fun pageListParse(response: Response): List<Page> {
        val document = response.asJsoup()
        return document.select("div.flex.flex-col.items-center img").mapIndexed { index, element ->
            Page(index, "", element.attr("src"))
        }
    }

    override fun imageUrlParse(response: Response): String {
        return ""
    }

    // Helper function
    private fun mangaFromElement(element: Element): SManga {
        return SManga.create().apply {
            title = element.selectFirst("h3 a")?.text() ?: ""
            thumbnail_url = element.selectFirst("img")?.attr("src") ?: ""
            url = element.selectFirst("h3 a")?.attr("href") ?: ""
        }
    }

    override fun getFilterList(): FilterList = FilterList()
}
