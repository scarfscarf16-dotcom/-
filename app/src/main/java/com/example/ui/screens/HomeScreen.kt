package com.example.ui.screens

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.toArabicDigits
import com.example.data.repository.QuranRepository
import com.example.ui.components.DURATION_PRESETS
import com.example.ui.theme.CairoFontFamily
import com.example.ui.theme.IslamicGold
import com.example.ui.theme.IslamicGoldLight
import com.example.ui.theme.IslamicGreenDark
import com.example.ui.theme.IslamicGreenMedium
import com.example.ui.theme.ObsidianBlack
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.SurfaceCardBorder
import com.example.ui.viewmodel.QuranStudioViewModel

@Composable
fun HomeScreen(
    viewModel: QuranStudioViewModel,
    onNavigateToStudio: () -> Unit,
    onNavigateToSaved: () -> Unit,
    modifier: Modifier = Modifier
) {
    val savedProjects by viewModel.savedProjects.collectAsState()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(ObsidianBlack),
        contentPadding = PaddingValues(bottom = 32.dp)
    ) {
        // Hero Section
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_bg_mosque),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    ObsidianBlack.copy(alpha = 0.5f),
                                    ObsidianBlack.copy(alpha = 0.85f),
                                    ObsidianBlack
                                )
                            )
                        )
                )

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.Bottom
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(IslamicGreenMedium.copy(alpha = 0.85f))
                            .border(1.dp, IslamicGold, RoundedCornerShape(20.dp))
                            .padding(horizontal = 12.dp, vertical = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = IslamicGoldLight,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "صانع مقاطع القرآن الاحترافي (60 ثانية+)",
                            fontFamily = CairoFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            color = IslamicGoldLight
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "توليد مقاطع قرآنية بدقة للمدة وتلاوة مكتملة",
                        fontFamily = CairoFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 19.sp,
                        lineHeight = 26.sp,
                        color = Color.White
                    )

                    Text(
                        text = "دمج تلاوات متتالية دون بتر أي آية + إنشاء إعدادات فيديو تلقائية",
                        fontFamily = CairoFontFamily,
                        fontSize = 12.sp,
                        color = Color.White.copy(alpha = 0.75f),
                        modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = {
                                viewModel.createAutoVideoSetup(targetDuration = 60, autoPlay = true)
                                onNavigateToStudio()
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = IslamicGold,
                                contentColor = Color.Black
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .weight(1.3f)
                                .height(44.dp)
                                .testTag("home_auto_setup_button")
                        ) {
                            Icon(Icons.Default.AutoAwesome, null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "توليد إعدادات تلقائية",
                                fontFamily = CairoFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }

                        Button(
                            onClick = onNavigateToStudio,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = SurfaceCard,
                                contentColor = Color.White
                            ),
                            border = androidx.compose.foundation.BorderStroke(1.dp, IslamicGoldLight),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(44.dp)
                                .testTag("home_open_studio_button")
                        ) {
                            Icon(Icons.Default.Videocam, null, tint = IslamicGold, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "الاستوديو",
                                fontFamily = CairoFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }
        }

        // Section 1: Quick Generate By Duration
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Timer,
                            contentDescription = null,
                            tint = IslamicGold,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "اختيار مدة الفيديو المحددة",
                            fontFamily = CairoFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(DURATION_PRESETS) { preset ->
                        Box(
                            modifier = Modifier
                                .testTag("home_duration_chip_${preset.seconds}")
                                .width(120.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(if (preset.isPrimary) IslamicGreenMedium else SurfaceCard)
                                .border(
                                    1.dp,
                                    if (preset.isPrimary) IslamicGold else SurfaceCardBorder,
                                    RoundedCornerShape(14.dp)
                                )
                                .clickable {
                                    viewModel.createAutoVideoSetup(targetDuration = preset.seconds, autoPlay = true)
                                    onNavigateToStudio()
                                }
                                .padding(12.dp)
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(if (preset.isPrimary) IslamicGold else IslamicGreenMedium),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "${preset.seconds}s",
                                        fontFamily = CairoFontFamily,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        color = if (preset.isPrimary) Color.Black else IslamicGoldLight
                                    )
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = preset.titleArabic,
                                    fontFamily = CairoFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = Color.White
                                )
                                Text(
                                    text = preset.tagArabic,
                                    fontFamily = CairoFontFamily,
                                    fontSize = 10.sp,
                                    color = IslamicGoldLight
                                )
                            }
                        }
                    }
                }
            }
        }

        // Section 2: Popular Curated Clips
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Book,
                            contentDescription = null,
                            tint = IslamicGold,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "مقاطع دقيقة كاملة (60 ثانية+) جاهزة",
                            fontFamily = CairoFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(QuranRepository.POPULAR_DURATION_CLIPS) { clip ->
                        Box(
                            modifier = Modifier
                                .testTag("popular_clip_${clip.surahNumber}")
                                .width(230.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(SurfaceCard)
                                .border(1.dp, SurfaceCardBorder, RoundedCornerShape(16.dp))
                                .clickable {
                                    viewModel.loadRecommendedClip(clip)
                                    onNavigateToStudio()
                                }
                                .padding(14.dp)
                        ) {
                            Column {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(IslamicGreenMedium)
                                            .padding(horizontal = 8.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            text = clip.category,
                                            fontFamily = CairoFontFamily,
                                            fontSize = 10.sp,
                                            color = IslamicGoldLight
                                        )
                                    }

                                    Text(
                                        text = "${clip.durationSeconds} ثانية",
                                        fontFamily = CairoFontFamily,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp,
                                        color = IslamicGold
                                    )
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                Text(
                                    text = clip.titleArabic,
                                    fontFamily = CairoFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = Color.White
                                )

                                Text(
                                    text = clip.description,
                                    fontFamily = CairoFontFamily,
                                    fontSize = 11.sp,
                                    color = Color.White.copy(alpha = 0.65f),
                                    lineHeight = 16.sp,
                                    modifier = Modifier.padding(top = 4.dp, bottom = 10.dp)
                                )

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(IslamicGreenDark)
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.PlayArrow,
                                        contentDescription = null,
                                        tint = IslamicGold,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "فتح في الاستوديو",
                                        fontFamily = CairoFontFamily,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp,
                                        color = IslamicGoldLight
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Section 3: Saved Video Projects
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.VideoLibrary,
                            contentDescription = null,
                            tint = IslamicGold,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "مقاطعي المحفوظة (${savedProjects.size.toArabicDigits()})",
                            fontFamily = CairoFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Color.White
                        )
                    }

                    if (savedProjects.isNotEmpty()) {
                        Text(
                            text = "عرض الكل",
                            fontFamily = CairoFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = IslamicGoldLight,
                            modifier = Modifier.clickable { onNavigateToSaved() }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                if (savedProjects.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(SurfaceCard)
                            .border(1.dp, SurfaceCardBorder, RoundedCornerShape(14.dp))
                            .padding(20.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.Videocam,
                                contentDescription = null,
                                tint = IslamicGold.copy(alpha = 0.5f),
                                modifier = Modifier.size(38.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "لم تقم بتصدير مقاطع بعد",
                                fontFamily = CairoFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = Color.White
                            )
                            Text(
                                text = "اضغط على زر «توليد إعدادات تلقائية» لصنع مقطعك الأول بنقرة واحدة",
                                fontFamily = CairoFontFamily,
                                fontSize = 11.sp,
                                color = Color.White.copy(alpha = 0.6f),
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    }
                } else {
                    savedProjects.take(3).forEach { project ->
                        Box(
                            modifier = Modifier
                                .testTag("saved_project_item_${project.id}")
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(SurfaceCard)
                                .border(1.dp, SurfaceCardBorder, RoundedCornerShape(12.dp))
                                .clickable {
                                    val surah = QuranRepository.getSurahById(project.surahNumber)
                                    if (surah != null) {
                                        viewModel.selectSurahAndRange(surah, project.fromAyah, project.toAyah)
                                        onNavigateToStudio()
                                    }
                                }
                                .padding(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = project.title,
                                        fontFamily = CairoFontFamily,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = Color.White
                                    )
                                    Text(
                                        text = "القارئ: ${project.reciterName} • المدة: ${project.durationSeconds} ثانية • ${project.aspectRatio}",
                                        fontFamily = CairoFontFamily,
                                        fontSize = 11.sp,
                                        color = IslamicGoldLight
                                    )
                                }

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    IconButton(onClick = { viewModel.shareSavedProject(project) }) {
                                        Icon(
                                            imageVector = Icons.Default.Share,
                                            contentDescription = "مشاركة",
                                            tint = IslamicGold,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }

                                    IconButton(onClick = { viewModel.deleteProject(project.id) }) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "حذف",
                                            tint = Color.White.copy(alpha = 0.5f),
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}
