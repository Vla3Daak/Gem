package com.example.animetv

data class Episode(
    val number: Int,
    val title: String,
    val streamUrl: String
)

data class AnimeItem(
    val id: String,
    val title: String,
    val category: String,
    val rating: String,
    val year: String,
    val posterUrl: String,
    val description: String,
    val episodes: List<Episode>
)

object MockAnimeRepository {
    private val sampleStream = "https://test-streams.mux.dev/x36xhzz/x36xhzz.m3u8"

    val animeList = listOf(
        AnimeItem(
            id = "1",
            title = "Провожающая в последний путь Фрирен",
            category = "Популярное",
            rating = "9.3",
            year = "2023",
            posterUrl = "https://shikimori.one/system/animes/original/52991.jpg",
            description = "Одержав победу над Королём Демонов, отряд героев вернулся домой. Эльфийка-маг Фрирен отправляется в новое путешествие, чтобы понять людей.",
            episodes = (1..12).map { Episode(it, "Эпизод $it", sampleStream) }
        ),
        AnimeItem(
            id = "2",
            title = "Поднятие уровня в одиночку",
            category = "Популярное",
            rating = "8.7",
            year = "2024",
            posterUrl = "https://shikimori.one/system/animes/original/52299.jpg",
            description = "Слабейший охотник человечества Сон Джину получает уникальную способность повышать свой уровень в подземельях.",
            episodes = (1..12).map { Episode(it, "Эпизод $it", sampleStream) }
        ),
        AnimeItem(
            id = "3",
            title = "Магическая битва",
            category = "Экшен и Фэнтези",
            rating = "8.6",
            year = "2020",
            posterUrl = "https://shikimori.one/system/animes/original/40748.jpg",
            description = "Старшеклассник Юдзи Итадори проглатывает проклятый артефакт и становится сосудом для могущественного проклятия Двуликого Сукуны.",
            episodes = (1..24).map { Episode(it, "Эпизод $it", sampleStream) }
        ),
        AnimeItem(
            id = "4",
            title = "Человек-бензопила",
            category = "Экшен и Фэнтези",
            rating = "8.5",
            year = "2022",
            posterUrl = "https://shikimori.one/system/animes/original/44511.jpg",
            description = "Дэндзи заключает контракт со своим питомцем-демоном Почитой и обретает способность превращать части своего тела в бензопилы.",
            episodes = (1..12).map { Episode(it, "Эпизод $it", sampleStream) }
        )
    )
}
