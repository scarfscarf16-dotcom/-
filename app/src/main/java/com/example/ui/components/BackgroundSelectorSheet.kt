package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.data.model.BackgroundPreset
import com.example.data.model.BackgroundType
import com.example.data.model.ParticleEffectType
import com.example.ui.theme.CairoFontFamily
import com.example.ui.theme.IslamicGold
import com.example.ui.theme.IslamicGoldLight
import com.example.ui.theme.IslamicGreenMedium
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.SurfaceCardBorder
import com.example.ui.theme.SurfaceDark

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BackgroundSelectorSheet(
    selectedPreset: BackgroundPreset,
    onPresetSelected: (BackgroundPreset) -> Unit,
    selectedParticle: ParticleEffectType,
    onParticleSelected: (ParticleEffectType) -> Unit,
    selectedAspectRatio: String,
    onAspectRatioSelected: (String) -> Unit,
    sheetState: SheetState,
    onDismissRequest: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        containerColor = SurfaceDark,
        dragHandle = null
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
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
                            text = "تخصيص الخلفية والمظهر",
                            fontFamily = CairoFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp,
                            color = Color.White
                        )
                        Text(
                            text = "خلفيات إسلامية ساحرة ومؤثرات سينمائية",
                            fontFamily = CairoFontFamily,
                            fontSize = 12.sp,
                            color = Color.White.copy(alpha = 0.65f)
                        )
                    }
                }

                IconButton(onClick = onDismissRequest) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "إغلاق",
                        tint = Color.White.copy(alpha = 0.7f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Aspect Ratio
            Text(
                text = "أبعاد الفيديو:",
                fontFamily = CairoFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = IslamicGoldLight
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                listOf(
                    Triple("9:16", "ستوري / ريلز", "عمودي 9:16"),
                    Triple("1:1", "مربع إنستغرام", "مربع 1:1"),
                    Triple("16:9", "يوتيوب / شاشات", "عرضي 16:9")
                ).forEach { (ratio, sub, label) ->
                    val isSelected = selectedAspectRatio == ratio
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isSelected) IslamicGold else SurfaceCard)
                            .border(
                                1.dp,
                                if (isSelected) IslamicGoldLight else SurfaceCardBorder,
                                RoundedCornerShape(12.dp)
                            )
                            .clickable { onAspectRatioSelected(ratio) }
                            .padding(vertical = 10.dp, horizontal = 6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = ratio,
                                fontFamily = CairoFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = if (isSelected) Color.Black else Color.White
                            )
                            Text(
                                text = sub,
                                fontFamily = CairoFontFamily,
                                fontSize = 10.sp,
                                color = if (isSelected) Color(0xFF222222) else Color.White.copy(alpha = 0.6f)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Background Presets
            Text(
                text = "الخلفية الإسلامية:",
                fontFamily = CairoFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = IslamicGoldLight
            )
            Spacer(modifier = Modifier.height(10.dp))
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(BackgroundPreset.ALL_PRESETS) { preset ->
                    val isSelected = selectedPreset.id == preset.id
                    Box(
                        modifier = Modifier
                            .testTag("bg_preset_${preset.id}")
                            .width(130.dp)
                            .height(160.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .border(
                                2.dp,
                                if (isSelected) IslamicGold else SurfaceCardBorder,
                                RoundedCornerShape(14.dp)
                            )
                            .clickable { onPresetSelected(preset) }
                    ) {
                        if (preset.type == BackgroundType.IMAGE_DRAWABLE && preset.drawableResId != null) {
                            Image(
                                painter = painterResource(id = preset.drawableResId),
                                contentDescription = preset.titleArabic,
                                modifier = Modifier.matchParentSize(),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Box(
                                modifier = Modifier
                                    .matchParentSize()
                                    .background(Brush.verticalGradient(preset.gradientColors))
                            )
                        }

                        Box(
                            modifier = Modifier
                                .matchParentSize()
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(
                                            Color.Transparent,
                                            Color.Black.copy(alpha = 0.85f)
                                        )
                                    )
                                )
                        )

                        if (isSelected) {
                            Box(
                                modifier = Modifier
                                    .padding(8.dp)
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(IslamicGold)
                                    .align(Alignment.TopEnd),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = Color.Black,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }

                        Column(
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(8.dp)
                        ) {
                            Text(
                                text = preset.titleArabic,
                                fontFamily = CairoFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                color = Color.White
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Particle Effects
            Text(
                text = "مؤثر الجزيئات والإضاءة:",
                fontFamily = CairoFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = IslamicGoldLight
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ParticleEffectType.values().forEach { particle ->
                    val isSelected = selectedParticle == particle
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (isSelected) IslamicGreenMedium else SurfaceCard)
                            .border(
                                1.dp,
                                if (isSelected) IslamicGold else SurfaceCardBorder,
                                RoundedCornerShape(10.dp)
                            )
                            .clickable { onParticleSelected(particle) }
                            .padding(vertical = 8.dp, horizontal = 4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = particle.labelArabic,
                            fontFamily = CairoFontFamily,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 11.sp,
                            color = if (isSelected) IslamicGoldLight else Color.White.copy(alpha = 0.7f),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
