package com.example.animetv

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.annotation.OptIn
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.tv.foundation.lazy.list.TvLazyColumn
import androidx.tv.foundation.lazy.list.TvLazyRow
import androidx.tv.foundation.lazy.list.items
import androidx.tv.material3.*
import coil.compose.AsyncImage

sealed class ScreenState {
    object Catalog : ScreenState()
    data class Details(val anime: AnimeItem) : ScreenState()
    data class Player(val anime: AnimeItem, val episode: Episode) : ScreenState()
}

class MainActivity : ComponentActivity() {
    @kotlin.OptIn(ExperimentalTvMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme(colorScheme = darkColorScheme()) {
                var screenState by remember { mutableStateOf<ScreenState>(ScreenState.Catalog) }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    shape = RectangleShape
                ) {
                    when (val state = screenState) {
                        is ScreenState.Catalog -> {
                            CatalogScreen(
                                animeList = MockAnimeRepository.animeList,
                                onAnimeClick = { anime -> screenState = ScreenState.Details(anime) }
                            )
                        }
                        is ScreenState.Details -> {
                            DetailsScreen(
                                anime = state.anime,
                                onEpisodeClick = { episode ->
                                    screenState = ScreenState.Player(state.anime, episode)
                                },
                                onBack = { screenState = ScreenState.Catalog }
                            )
                        }
                        is ScreenState.Player -> {
                            VideoPlayerScreen(
                                streamUrl = state.episode.streamUrl,
                                onBack = { screenState = ScreenState.Details(state.anime) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@kotlin.OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun CatalogScreen(
    animeList: List<AnimeItem>,
    onAnimeClick: (AnimeItem) -> Unit
) {
    var focusedAnime by remember { mutableStateOf(animeList.first()) }
    val groupedAnime = remember(animeList) { animeList.groupBy { it.category } }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F0F14))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(230.dp)
                .padding(horizontal = 48.dp, vertical = 24.dp),
            contentAlignment = Alignment.BottomStart
        ) {
            Column(modifier = Modifier.fillMaxWidth(0.7f)) {
                Text(
                    text = focusedAnime.title,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "★ ${focusedAnime.rating}  •  ${focusedAnime.year}  •  ${focusedAnime.episodes.size} эп.",
                    fontSize = 14.sp,
                    color = Color(0xFFFF9800),
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = focusedAnime.description,
                    fontSize = 15.sp,
                    color = Color(0xFFB0B0C0),
                    maxLines = 3
                )
            }
        }

        TvLazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {
            groupedAnime.forEach { (category, items) ->
                item {
                    Text(
                        text = category,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White,
                        modifier = Modifier.padding(start = 48.dp, bottom = 12.dp, top = 16.dp)
                    )
                    TvLazyRow(
                        contentPadding = PaddingValues(horizontal = 48.dp),
                        horizontalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        items(items) { anime ->
                            AnimeCard(
                                anime = anime,
                                onFocus = { focusedAnime = anime },
                                onClick = { onAnimeClick(anime) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@kotlin.OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun DetailsScreen(
    anime: AnimeItem,
    onEpisodeClick: (Episode) -> Unit,
    onBack: () -> Unit
) {
    BackHandler(onBack = onBack)

    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F0F14))
            .padding(48.dp)
    ) {
        AsyncImage(
            model = anime.posterUrl,
            contentDescription = anime.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .width(240.dp)
                .height(350.dp)
        )

        Spacer(modifier = Modifier.width(36.dp))

        Column(modifier = Modifier.fillMaxSize()) {
            Text(
                text = anime.title,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Рейтинг: ★ ${anime.rating}   |   Год: ${anime.year}",
                fontSize = 15.sp,
                color = Color(0xFFFF9800)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = anime.description,
                fontSize = 15.sp,
                color = Color.LightGray
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Выберите серию:",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(12.dp))

            TvLazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(anime.episodes) { episode ->
                    Button(
                        onClick = { onEpisodeClick(episode) }
                    ) {
                        Text(text = "Серия ${episode.number}")
                    }
                }
            }
        }
    }
}

@kotlin.OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun AnimeCard(
    anime: AnimeItem,
    onFocus: () -> Unit,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .width(150.dp)
            .height(220.dp)
            .onFocusChanged { if (it.isFocused) onFocus() },
        scale = CardDefaults.scale(focusedScale = 1.08f),
        border = CardDefaults.border(
            focusedBorder = Border(
                border = BorderStroke(3.dp, Color(0xFFFF5722))
            )
        )
    ) {
        AsyncImage(
            model = anime.posterUrl,
            contentDescription = anime.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@OptIn(UnstableApi::class)
@Composable
fun VideoPlayerScreen(
    streamUrl: String,
    onBack: () -> Unit
) {
    val context = LocalContext.current

    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(streamUrl))
            prepare()
            playWhenReady = true
        }
    }

    BackHandler(onBack = onBack)

    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }

    AndroidView(
        factory = { ctx ->
            PlayerView(ctx).apply {
                player = exoPlayer
                useController = true
                requestFocus()
            }
        },
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    )
}
