package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.toArabicDigits
import com.example.ui.components.BackgroundSelectorSheet
import com.example.ui.components.DurationSelectorView
import com.example.ui.components.ExportProgressDialog
import com.example.ui.components.QuranVideoCanvas
import com.example.ui.components.ReciterSelectorSheet
import com.example.ui.components.SurahAyahPickerSheet
import com.example.ui.theme.CairoFontFamily
import com.example.ui.theme.IslamicGold
import com.example.ui.theme.IslamicGoldLight
import com.example.ui.theme.IslamicGreenMedium
import com.example.ui.theme.ObsidianBlack
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.SurfaceCardBorder
import com.example.ui.viewmodel.QuranStudioViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoGeneratorScreen(
    viewModel: QuranStudioViewModel,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val surah by viewModel.currentSurah.collectAsState()
    val fromAyah by viewModel.fromAyah.collectAsState()
    val toAyah by viewModel.toAyah.collectAsState()
    val ayahs by viewModel.currentAyahs.collectAsState()
    val targetDuration by viewModel.targetDurationSeconds.collectAsState()
    val actualDuration by viewModel.actualDurationSeconds.collectAsState()
    val reciter by viewModel.currentReciter.collectAsState()
    val backgroundPreset by viewModel.currentBackgroundPreset.collectAsState()
    val particleType by viewModel.currentParticleType.collectAsState()
    val aspectRatio by viewModel.currentAspectRatio.collectAsState()
    val showTranslation by viewModel.showTranslation.collectAsState()
    val showBasmala by viewModel.showBasmala.collectAsState()
    val autoSetupBanner by viewModel.autoSetupBanner.collectAsState()

    val isPlaying by viewModel.isPlaying.collectAsState()
    val currentAyahIndex by viewModel.currentAyahIndex.collectAsState()
    val currentPositionMs by viewModel.currentPositionMs.collectAsState()
    val totalDurationMs by viewModel.totalDurationMs.collectAsState()
    val clipElapsedMs by viewModel.clipElapsedMs.collectAsState()
    val clipTotalDurationMs by viewModel.clipTotalDurationMs.collectAsState()
    val isCachedLocally by viewModel.isCachedLocally.collectAsState()
    val isBuffering by viewModel.isBuffering.collectAsState()
    val exportState by viewModel.exportState.collectAsState()
    val volumeLevel by viewModel.volumeLevel.collectAsState()
    val audioErrorMessage by viewModel.audioErrorMessage.collectAsState()

    var showSurahSheet by remember { mutableStateOf(false) }
    var showReciterSheet by remember { mutableStateOf(false) }
    var showBackgroundSheet by remember { mutableStateOf(false) }

    val surahSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val reciterSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val bgSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val currentAyah = ayahs.getOrNull(currentAyahIndex) ?: ayahs.firstOrNull()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(ObsidianBlack)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 90.dp)
        ) {
            // Top App Bar
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                            onClick = onNavigateBack,
                            modifier = Modifier.testTag("studio_back_button")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "رجوع",
                                tint = IslamicGold
                            )
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                        Column {
                            Text(
                                text = "استوديو توليد الفيديو القرآني",
                                fontFamily = CairoFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = Color.White
                            )
                            Text(
                                text = "توليد دقيق للمدة مع دمج الآيات دون بتر",
                                fontFamily = CairoFontFamily,
                                fontSize = 11.sp,
                                color = IslamicGoldLight
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(IslamicGreenMedium)
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = aspectRatio,
                            fontFamily = CairoFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = IslamicGoldLight
                        )
                    }
                }
            }

            // Automatic Configuration Banner if triggered
            item {
                AnimatedVisibility(visible = autoSetupBanner != null) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 4.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(IslamicGreenMedium)
                            .border(1.dp, IslamicGold, RoundedCornerShape(12.dp))
                            .padding(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                modifier = Modifier.weight(1f),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = IslamicGoldLight,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = autoSetupBanner ?: "",
                                    fontFamily = CairoFontFamily,
                                    fontSize = 11.sp,
                                    color = Color.White,
                                    lineHeight = 16.sp
                                )
                            }
                            IconButton(
                                onClick = { viewModel.dismissAutoSetupBanner() },
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(Icons.Default.Close, null, tint = Color.White, modifier = Modifier.size(16.dp))
                            }
                        }
                    }
                }
            }

            // Video Canvas Preview Area
            item {
                if (currentAyah != null) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        QuranVideoCanvas(
                            surah = surah,
                            currentAyah = currentAyah,
                            currentAyahIndex = currentAyahIndex,
                            totalAyahsCount = ayahs.size,
                            reciter = reciter,
                            backgroundPreset = backgroundPreset,
                            particleType = particleType,
                            aspectRatio = aspectRatio,
                            isPlaying = isPlaying,
                            showTranslation = showTranslation,
                            showBasmala = showBasmala,
                            totalDurationSeconds = actualDuration,
                            onTogglePlayPause = { viewModel.togglePlayPause() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(if (aspectRatio == "16:9") 220.dp else if (aspectRatio == "1:1") 340.dp else 430.dp)
                        )
                    }
                }
            }

            // Playback Progress & Control Bar
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 8.dp)
                ) {
                    val overallFraction = if (clipTotalDurationMs > 0) {
                        (clipElapsedMs.toFloat() / clipTotalDurationMs.toFloat()).coerceIn(0f, 1f)
                    } else if (totalDurationMs > 0) {
                        (currentPositionMs.toFloat() / totalDurationMs.toFloat()).coerceIn(0f, 1f)
                    } else 0f

                    LinearProgressIndicator(
                        progress = { overallFraction },
                        color = IslamicGold,
                        trackColor = SurfaceCardBorder,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp))
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "الآية ${(currentAyahIndex + 1).toArabicDigits()} من ${ayahs.size.toArabicDigits()} • سورة ${surah.nameArabic}",
                                fontFamily = CairoFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = IslamicGoldLight
                            )
                            val currentSec = (clipElapsedMs / 1000).coerceAtLeast(0)
                            val totalSec = if (clipTotalDurationMs > 0) clipTotalDurationMs / 1000 else actualDuration
                            val minCur = currentSec / 60
                            val secCur = currentSec % 60
                            val minTot = totalSec / 60
                            val secTot = totalSec % 60
                            val timeStr = String.format("%02d:%02d / %02d:%02d", minCur, secCur, minTot, secTot)
                            Text(
                                text = timeStr,
                                fontFamily = CairoFontFamily,
                                fontSize = 11.sp,
                                color = Color.White.copy(alpha = 0.75f)
                            )
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            IconButton(
                                onClick = { viewModel.skipToPreviousAyah() },
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.SkipPrevious,
                                    contentDescription = "الآية السابقة",
                                    tint = IslamicGold
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .size(46.dp)
                                    .clip(CircleShape)
                                    .background(IslamicGold)
                                    .clickable { viewModel.togglePlayPause() },
                                contentAlignment = Alignment.Center
                            ) {
                                if (isBuffering) {
                                    CircularProgressIndicator(
                                        color = Color.Black,
                                        modifier = Modifier.size(22.dp),
                                        strokeWidth = 2.dp
                                    )
                                } else {
                                    Icon(
                                        imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                        contentDescription = if (isPlaying) "إيقاف مؤقت" else "تشغيل",
                                        tint = Color.Black,
                                        modifier = Modifier.size(28.dp)
                                    )
                                }
                            }

                            IconButton(
                                onClick = { viewModel.skipToNextAyah() },
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.SkipNext,
                                    contentDescription = "الآية التالية",
                                    tint = IslamicGold
                                )
                            }
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "${actualDuration.toArabicDigits()}ث (مكتمل)",
                                fontFamily = CairoFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = IslamicGoldLight
                            )
                            Text(
                                text = "تلاوة تامة دون بتر",
                                fontFamily = CairoFontFamily,
                                fontSize = 10.sp,
                                color = Color.White.copy(alpha = 0.6f)
                            )
                        }
                    }
                }
            }

            // Audio Volume & Unmute Helper Card
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(SurfaceCard)
                        .border(1.dp, SurfaceCardBorder, RoundedCornerShape(14.dp))
                        .padding(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = if (volumeLevel > 0f) Icons.Default.VolumeUp else Icons.Default.VolumeOff,
                                contentDescription = null,
                                tint = IslamicGold,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "مستوى صوت التلاوة: ${(volumeLevel * 100).toInt()}%",
                                fontFamily = CairoFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = Color.White
                            )
                        }

                        Button(
                            onClick = { viewModel.unmuteAndMaximizeVolume() },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = IslamicGreenMedium,
                                contentColor = IslamicGoldLight
                            ),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                            modifier = Modifier
                                .height(30.dp)
                                .testTag("unmute_and_boost_volume_button")
                        ) {
                            Text(
                                text = "🔊 رفع وتفعيل الصوت",
                                fontFamily = CairoFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Slider(
                        value = volumeLevel,
                        onValueChange = { viewModel.setVolume(it) },
                        colors = SliderDefaults.colors(
                            thumbColor = IslamicGold,
                            activeTrackColor = IslamicGold,
                            inactiveTrackColor = SurfaceCardBorder
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(28.dp)
                            .testTag("audio_volume_slider")
                    )

                    if (isBuffering) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            CircularProgressIndicator(
                                color = IslamicGold,
                                strokeWidth = 2.dp,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "جارٍ حفظ وتجهيز تلاوة الآية التالية بسلاسة...",
                                fontFamily = CairoFontFamily,
                                fontSize = 11.sp,
                                color = IslamicGoldLight
                            )
                        }
                    }

                    if (audioErrorMessage != null) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFF3E1616))
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = audioErrorMessage ?: "",
                                fontFamily = CairoFontFamily,
                                fontSize = 11.sp,
                                color = Color(0xFFFF8A80),
                                modifier = Modifier.weight(1f)
                            )
                            IconButton(
                                onClick = { viewModel.seekToAyah(currentAyahIndex) },
                                modifier = Modifier.size(28.dp)
                            ) {
                                Icon(Icons.Default.Refresh, contentDescription = "إعادة المحاولة", tint = Color.White)
                            }
                        }
                    }
                }
            }

            // USER FEATURE: "إنشاء إعدادات فيديو بشكل تلقائي"
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(SurfaceCard)
                        .border(1.5.dp, IslamicGold, RoundedCornerShape(16.dp))
                        .padding(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = IslamicGold,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "إنشاء إعدادات فيديو بشكل تلقائي",
                                fontFamily = CairoFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = Color.White
                            )
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(IslamicGreenMedium)
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "تلقائي ذكي ✦",
                                fontFamily = CairoFontFamily,
                                fontSize = 10.sp,
                                color = IslamicGoldLight
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "توليد تلقائي متكامل: يختار السورة ويدمج آياتها بالترتيب حتى تصل إلى 60 ثانية أو أكثر لإتمام الآية الأخيرة شرعاً دون بتر، مع القارئ والخلفية المناسبة وتشغيل الصوت مباشرة.",
                        fontFamily = CairoFontFamily,
                        fontSize = 11.sp,
                        color = Color.White.copy(alpha = 0.75f),
                        lineHeight = 16.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Button(
                        onClick = { viewModel.createAutoVideoSetup(targetDuration = 60, autoPlay = true) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = IslamicGold,
                            contentColor = Color.Black
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .testTag("auto_generate_video_setup_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "⚡ توليد إعدادات 60 ثانية تلقائياً وبدء التلاوة",
                            fontFamily = CairoFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { viewModel.createAutoVideoSetup(targetDuration = 30, autoPlay = true) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = SurfaceCardBorder,
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(36.dp)
                                .testTag("auto_setup_30s_button")
                        ) {
                            Text(
                                text = "٣٠ث (حالة)",
                                fontFamily = CairoFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            )
                        }

                        Button(
                            onClick = { viewModel.createAutoVideoSetup(targetDuration = 60, autoPlay = true) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = IslamicGreenMedium,
                                contentColor = IslamicGoldLight
                            ),
                            border = androidx.compose.foundation.BorderStroke(1.dp, IslamicGold),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .weight(1.2f)
                                .height(36.dp)
                                .testTag("auto_setup_60s_button")
                        ) {
                            Text(
                                text = "٦٠ث (ريلز كامل)",
                                fontFamily = CairoFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            )
                        }

                        Button(
                            onClick = { viewModel.createAutoVideoSetup(targetDuration = 90, autoPlay = true) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = SurfaceCardBorder,
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(36.dp)
                                .testTag("auto_setup_90s_button")
                        ) {
                            Text(
                                text = "٩٠ث (مطول)",
                                fontFamily = CairoFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }

            // Duration Selector View
            item {
                Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)) {
                    DurationSelectorView(
                        selectedDurationSeconds = targetDuration,
                        actualDurationSeconds = actualDuration,
                        onDurationSelected = { duration ->
                            viewModel.setTargetDuration(duration)
                        }
                    )
                }
            }

            // Quran Library & Ayah Range Selector Card
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(SurfaceCard)
                        .border(1.dp, SurfaceCardBorder, RoundedCornerShape(14.dp))
                        .clickable { showSurahSheet = true }
                        .padding(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(IslamicGreenMedium),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Book,
                                    contentDescription = null,
                                    tint = IslamicGold,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "مكتبة الآيات القرآنية",
                                    fontFamily = CairoFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = Color.White
                                )
                                Text(
                                    text = "سورة ${surah.nameArabic} (الآيات $fromAyah - $toAyah) • ${actualDuration}ثانية",
                                    fontFamily = CairoFontFamily,
                                    fontSize = 12.sp,
                                    color = IslamicGoldLight
                                )
                            }
                        }

                        Button(
                            onClick = { showSurahSheet = true },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = SurfaceCardBorder,
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                            modifier = Modifier.height(32.dp)
                        ) {
                            Text(
                                text = "تغيير",
                                fontFamily = CairoFontFamily,
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }

            // Reciter Selector Card
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(SurfaceCard)
                        .border(1.dp, SurfaceCardBorder, RoundedCornerShape(14.dp))
                        .clickable { showReciterSheet = true }
                        .padding(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(IslamicGreenMedium),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.RecordVoiceOver,
                                    contentDescription = null,
                                    tint = IslamicGold,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "القارئ والتلاوة",
                                    fontFamily = CairoFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = Color.White
                                )
                                Text(
                                    text = "${reciter.nameArabic} • ${reciter.subStyle}",
                                    fontFamily = CairoFontFamily,
                                    fontSize = 12.sp,
                                    color = IslamicGoldLight
                                )
                            }
                        }

                        Button(
                            onClick = { showReciterSheet = true },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = SurfaceCardBorder,
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                            modifier = Modifier.height(32.dp)
                        ) {
                            Text(
                                text = "تغيير",
                                fontFamily = CairoFontFamily,
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }

            // Background & Visuals Card
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(SurfaceCard)
                        .border(1.dp, SurfaceCardBorder, RoundedCornerShape(14.dp))
                        .clickable { showBackgroundSheet = true }
                        .padding(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(IslamicGreenMedium),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Palette,
                                    contentDescription = null,
                                    tint = IslamicGold,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "الخلفية والمؤثرات البصرية",
                                    fontFamily = CairoFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = Color.White
                                )
                                Text(
                                    text = "${backgroundPreset.titleArabic} • ${particleType.labelArabic} • $aspectRatio",
                                    fontFamily = CairoFontFamily,
                                    fontSize = 12.sp,
                                    color = IslamicGoldLight
                                )
                            }
                        }

                        Button(
                            onClick = { showBackgroundSheet = true },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = SurfaceCardBorder,
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                            modifier = Modifier.height(32.dp)
                        ) {
                            Text(
                                text = "تخصيص",
                                fontFamily = CairoFontFamily,
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }

            // Quick Display Toggles
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(SurfaceCard)
                        .border(1.dp, SurfaceCardBorder, RoundedCornerShape(14.dp))
                        .padding(14.dp)
                ) {
                    Text(
                        text = "خيارات النصوص والترجمة:",
                        fontFamily = CairoFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = IslamicGoldLight
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "إظهار البسملة الشريفة ﷽",
                            fontFamily = CairoFontFamily,
                            fontSize = 13.sp,
                            color = Color.White
                        )
                        Switch(
                            checked = showBasmala,
                            onCheckedChange = { viewModel.toggleBasmala() },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.Black,
                                checkedTrackColor = IslamicGold
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "إظهار التفسير الميسر / الترجمة",
                            fontFamily = CairoFontFamily,
                            fontSize = 13.sp,
                            color = Color.White
                        )
                        Switch(
                            checked = showTranslation,
                            onCheckedChange = { viewModel.toggleTranslation() },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.Black,
                                checkedTrackColor = IslamicGold
                            )
                        )
                    }
                }
            }
        }

        // Floating Bottom Export Button
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(ObsidianBlack.copy(alpha = 0.95f))
                .border(1.dp, SurfaceCardBorder)
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Button(
                onClick = { viewModel.exportVideo() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = IslamicGold,
                    contentColor = Color.Black
                ),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("export_video_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Videocam,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "تصدير الفيديو التام ($actualDuration ثانية)",
                    fontFamily = CairoFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            }
        }

        if (showSurahSheet) {
            SurahAyahPickerSheet(
                selectedSurah = surah,
                fromAyah = fromAyah,
                toAyah = toAyah,
                onSelectionConfirmed = { newSurah, from, to ->
                    viewModel.selectSurahAndRange(newSurah, from, to)
                },
                onAutoFitDuration = { newSurah, startAyah ->
                    viewModel.autoFitDurationForSurah(newSurah, startAyah)
                },
                sheetState = surahSheetState,
                onDismissRequest = { showSurahSheet = false }
            )
        }

        if (showReciterSheet) {
            ReciterSelectorSheet(
                selectedReciter = reciter,
                onReciterSelected = { newReciter ->
                    viewModel.setReciter(newReciter)
                },
                sheetState = reciterSheetState,
                onDismissRequest = { showReciterSheet = false }
            )
        }

        if (showBackgroundSheet) {
            BackgroundSelectorSheet(
                selectedPreset = backgroundPreset,
                onPresetSelected = { newPreset ->
                    viewModel.setBackgroundPreset(newPreset)
                },
                selectedParticle = particleType,
                onParticleSelected = { newParticle ->
                    viewModel.setParticleType(newParticle)
                },
                selectedAspectRatio = aspectRatio,
                onAspectRatioSelected = { newRatio ->
                    viewModel.setAspectRatio(newRatio)
                },
                sheetState = bgSheetState,
                onDismissRequest = { showBackgroundSheet = false }
            )
        }

        ExportProgressDialog(
            exportState = exportState,
            onShareClicked = { viewModel.shareExportedVideo() },
            onDismiss = { viewModel.dismissExportDialog() }
        )
    }
}
