package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.ui.theme.CairoFontFamily
import com.example.ui.theme.IslamicGold
import com.example.ui.theme.IslamicGoldLight
import com.example.ui.theme.IslamicGreenMedium
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.SurfaceCardBorder

data class DurationPreset(
    val seconds: Int,
    val titleArabic: String,
    val tagArabic: String,
    val isPrimary: Boolean = false
)

val DURATION_PRESETS = listOf(
    DurationPreset(60, "60 ثانية", "دقيقة كاملة (ريلز)", isPrimary = true),
    DurationPreset(30, "30 ثانية", "حالة واتساب"),
    DurationPreset(45, "45 ثانية", "تيك توك"),
    DurationPreset(15, "15 ثانية", "ستوري قصير"),
    DurationPreset(90, "90 ثانية", "مقطع متوسط"),
    DurationPreset(120, "دقيقتان", "تلاوة مطولة")
)

@Composable
fun DurationSelectorView(
    selectedDurationSeconds: Int,
    actualDurationSeconds: Int,
    onDurationSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var showCustomSlider by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(SurfaceCard)
            .border(1.dp, SurfaceCardBorder, RoundedCornerShape(16.dp))
            .padding(16.dp)
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
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "مدة الفيديو القرآني",
                        fontFamily = CairoFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = Color.White
                    )
                    Text(
                        text = "المستهدف: ${selectedDurationSeconds.toArabicDigits()}ث • الفعلي المكتمل: ${actualDurationSeconds.toArabicDigits()}ث",
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
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = IslamicGoldLight,
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "اكتمال الآية دون بتر",
                        fontFamily = CairoFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp,
                        color = IslamicGoldLight
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(DURATION_PRESETS) { preset ->
                val isSelected = selectedDurationSeconds == preset.seconds && !showCustomSlider
                Box(
                    modifier = Modifier
                        .testTag("duration_chip_${preset.seconds}")
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isSelected) IslamicGold else SurfaceCard.copy(alpha = 0.8f))
                        .border(
                            1.dp,
                            if (isSelected) IslamicGoldLight else SurfaceCardBorder,
                            RoundedCornerShape(12.dp)
                        )
                        .clickable {
                            showCustomSlider = false
                            onDurationSelected(preset.seconds)
                        }
                        .padding(horizontal = 14.dp, vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = preset.titleArabic,
                            fontFamily = CairoFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = if (isSelected) Color.Black else Color.White
                        )
                        Text(
                            text = preset.tagArabic,
                            fontFamily = CairoFontFamily,
                            fontSize = 10.sp,
                            color = if (isSelected) Color(0xFF222222) else Color.White.copy(alpha = 0.6f)
                        )
                    }
                }
            }

            item {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (showCustomSlider) IslamicGold else SurfaceCard)
                        .border(
                            1.dp,
                            if (showCustomSlider) IslamicGoldLight else SurfaceCardBorder,
                            RoundedCornerShape(12.dp)
                        )
                        .clickable { showCustomSlider = !showCustomSlider }
                        .padding(horizontal = 14.dp, vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "تخصيص حر",
                            fontFamily = CairoFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = if (showCustomSlider) Color.Black else Color.White
                        )
                        Text(
                            text = "$selectedDurationSeconds ثانية",
                            fontFamily = CairoFontFamily,
                            fontSize = 10.sp,
                            color = if (showCustomSlider) Color(0xFF222222) else IslamicGold
                        )
                    }
                }
            }
        }

        AnimatedVisibility(visible = showCustomSlider) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "المدة المستهدفة للفيديو:",
                        fontFamily = CairoFontFamily,
                        fontSize = 12.sp,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                    Text(
                        text = "$selectedDurationSeconds ثانية",
                        fontFamily = CairoFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = IslamicGold
                    )
                }

                Slider(
                    value = selectedDurationSeconds.toFloat(),
                    onValueChange = { onDurationSelected(it.toInt()) },
                    valueRange = 10f..180f,
                    steps = 16,
                    colors = SliderDefaults.colors(
                        thumbColor = IslamicGold,
                        activeTrackColor = IslamicGold,
                        inactiveTrackColor = SurfaceCardBorder
                    ),
                    modifier = Modifier.testTag("custom_duration_slider")
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Important Islamic note
        Text(
            text = "💡 تنبيه شرعي: تُدمج الآيات كاملة حتى تصل إلى المدة المستهدفة أو تزيد بضع ثوانٍ لإتمام الآية الأخيرة، لأنه لا يجوز قطع الآية في منتصفها.",
            fontFamily = CairoFontFamily,
            fontSize = 11.sp,
            color = Color.White.copy(alpha = 0.65f),
            lineHeight = 16.sp
        )
    }
}
