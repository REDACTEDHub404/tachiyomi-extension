package eu.kanade.tachiyomi.extension.th.bullymanga

import eu.kanade.tachiyomi.multisrc.mangathemesia.MangaThemesia
import eu.kanade.tachiyomi.source.model.SManga

class BullyManga : MangaThemesia(
    "Bully-Manga",
    "https://bully-manga.com",
    "th"
) {
    // กำหนดรูปแบบการดึงสถานะของมังงะ (จบแล้ว / ยังไม่จบ)
    override val seriesStatusSelector = ".tsinfo .imethod:contains(สถานะ) i"
}