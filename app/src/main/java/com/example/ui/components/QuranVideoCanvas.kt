package com.example.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Ayah
import com.example.data.model.BackgroundPreset
import com.example.data.model.BackgroundType
import com.example.data.model.ParticleEffectType
import com.example.data.model.Reciter
import com.example.data.model.Surah
import com.example.data.model.toArabicDigits
import com.example.ui.theme.AmiriFontFamily
import com.example.ui.theme.CairoFontFamily
import com.example.ui.theme.IslamicGold
import com.example.ui.theme.IslamicGoldLight
import com.example.ui.theme.ObsidianBlack

@Composable
fun QuranVideoCanvas(
    surah: Surah,
    currentAyah: Ayah,
    currentAyahIndex: Int,
    totalAyahsCount: Int,
    reciter: Reciter,
    backgroundPreset: BackgroundPreset,
    particleType: ParticleEffectType,
    aspectRatio: String,
    isPlaying: Boolean,
    showTranslation: Boolean,
    showBasmala: Boolean,
    totalDurationSeconds: Int,
    onTogglePlayPause: () -> Unit,
    modifier: Modifier = Modifier
) {
    val ratioValue = when (aspectRatio) {
        "1:1" -> 1.0f
        "16:9" -> 16f / 9f
        else -> 9f / 16f
    }

    val infiniteTransition = rememberInfiniteTransition(label = "ken_burns")
    val bgScale by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = 1.06f,
        animationSpec = infiniteRepeatable(
            animation = tween(12000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "bg_zoom"
    )

    BoxWithConstraints(
        modifier = modifier
            .testTag("quran_video_canvas_container")
            .clip(RoundedCornerShape(16.dp))
            .border(1.5.dp, IslamicGold.copy(alpha = 0.45f), RoundedCornerShape(16.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                onTogglePlayPause()
            },
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .aspectRatio(ratioValue, matchHeightConstraintsFirst = true)
                .fillMaxSize()
                .clip(RoundedCornerShape(16.dp))
        ) {
            // Layer 1: Background
            if (backgroundPreset.type == BackgroundType.IMAGE_DRAWABLE && backgroundPreset.drawableResId != null) {
                Image(
                    painter = painterResource(id = backgroundPreset.drawableResId),
                    contentDescription = backgroundPreset.titleArabic,
                    modifier = Modifier
                        .fillMaxSize()
                        .scale(bgScale),
                    contentScale = ContentScale.Crop
                )
            } else {
                val colors = if (backgroundPreset.gradientColors.isNotEmpty()) {
                    backgroundPreset.gradientColors
                } else {
                    listOf(Color(0xFF071E17), Color(0xFF091215), Color(0xFF1B3B2B))
                }
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Brush.verticalGradient(colors = colors))
                )
            }

            // Layer 2: Reverence Dark Overlay
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                ObsidianBlack.copy(alpha = 0.60f),
                                ObsidianBlack.copy(alpha = backgroundPreset.overlayAlpha),
                                ObsidianBlack.copy(alpha = 0.75f)
                            )
                        )
                    )
            )

            // Layer 3: Particles
            IslamicParticleCanvas(
                particleType = particleType,
                modifier = Modifier.fillMaxSize()
            )

            // Layer 4: Quran Content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 18.dp, vertical = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Top Header: Surah and Basmala
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(ObsidianBlack.copy(alpha = 0.65f))
                            .border(1.dp, IslamicGold.copy(alpha = 0.5f), RoundedCornerShape(20.dp))
                            .padding(horizontal = 14.dp, vertical = 5.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "﴿ سورة ${surah.nameArabic} ﴾",
                                fontFamily = CairoFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = IslamicGoldLight
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "• ${surah.revelationType.arabicLabel}",
                                fontFamily = CairoFontFamily,
                                fontSize = 11.sp,
                                color = Color.White.copy(alpha = 0.75f)
                            )
                        }
                    }

                    if (showBasmala && surah.id != 9) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ",
                            fontFamily = AmiriFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 19.sp,
                            color = IslamicGold.copy(alpha = 0.95f),
                            textAlign = TextAlign.Center,
                            style = TextStyle(
                                shadow = Shadow(
                                    color = IslamicGold.copy(alpha = 0.4f),
                                    offset = Offset(0f, 2f),
                                    blurRadius = 6f
                                )
                            )
                        )
                    }
                }

                // Centerpiece: Ayah Text with Calligraphy
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    AnimatedContent(
                        targetState = currentAyah,
                        transitionSpec = {
                            (fadeIn(animationSpec = tween(350)) + slideInVertically { height -> height / 5 })
                                .togetherWith(fadeOut(animationSpec = tween(250)) + slideOutVertically { height -> -height / 5 })
                        },
                        label = "ayah_transition"
                    ) { targetAyah ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 6.dp)
                        ) {
                            Text(
                                text = targetAyah.textArabic,
                                fontFamily = AmiriFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = if (targetAyah.textArabic.length > 120) 21.sp else 27.sp,
                                lineHeight = if (targetAyah.textArabic.length > 120) 36.sp else 44.sp,
                                color = Color.White,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 4.dp),
                                style = TextStyle(
                                    shadow = Shadow(
                                        color = Color.Black.copy(alpha = 0.85f),
                                        offset = Offset(0f, 4f),
                                        blurRadius = 12f
                                    )
                                )
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Box(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(IslamicGold.copy(alpha = 0.2f))
                                    .border(1.dp, IslamicGold.copy(alpha = 0.7f), CircleShape)
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "۝ ${targetAyah.formattedAyahNumber}",
                                    fontFamily = CairoFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = IslamicGoldLight
                                )
                            }

                            if (showTranslation && targetAyah.translationArabicSimplified.isNotBlank()) {
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = targetAyah.translationArabicSimplified,
                                    fontFamily = CairoFontFamily,
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 12.sp,
                                    lineHeight = 17.sp,
                                    color = Color.White.copy(alpha = 0.85f),
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier
                                        .fillMaxWidth(0.92f)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(ObsidianBlack.copy(alpha = 0.45f))
                                        .padding(horizontal = 10.dp, vertical = 5.dp)
                                )
                            }
                        }
                    }
                }

                // Bottom Footer: Audio Wave + Reciter & Timing
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    AudioVisualizerView(
                        isPlaying = isPlaying,
                        barCount = 20,
                        maxHeight = 18.dp,
                        barColor = IslamicGold,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(ObsidianBlack.copy(alpha = 0.6f))
                            .padding(horizontal = 12.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Mic,
                                contentDescription = null,
                                tint = IslamicGold,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = reciter.nameArabic,
                                fontFamily = CairoFontFamily,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 12.sp,
                                color = Color.White
                            )
                        }

                        Text(
                            text = "آية ${(currentAyahIndex + 1).toArabicDigits()} من ${totalAyahsCount.toArabicDigits()} • ${totalDurationSeconds.toArabicDigits()}ث",
                            fontFamily = CairoFontFamily,
                            fontWeight = FontWeight.Medium,
                            fontSize = 11.sp,
                            color = IslamicGoldLight
                        )
                    }
                }
            }

            if (!isPlaying) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.22f)),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .background(ObsidianBlack.copy(alpha = 0.75f))
                            .border(1.5.dp, IslamicGold, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "تشغيل",
                            tint = IslamicGold,
                            modifier = Modifier.size(34.dp)
                        )
                    }
                }
            }
        }
    }
}
