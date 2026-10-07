package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ui.theme.IslamicGold

@Composable
fun AudioVisualizerView(
    isPlaying: Boolean,
    modifier: Modifier = Modifier,
    barCount: Int = 18,
    maxHeight: Dp = 22.dp,
    barWidth: Dp = 3.dp,
    barColor: Color = IslamicGold
) {
    val infiniteTransition = rememberInfiniteTransition(label = "audio_bars")

    val anim1 by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 0.95f,
        animationSpec = infiniteRepeatable(
            animation = tween(420, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "bar1"
    )

    val anim2 by infiniteTransition.animateFloat(
        initialValue = 0.35f,
        targetValue = 0.75f,
        animationSpec = infiniteRepeatable(
            animation = tween(310, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "bar2"
    )

    val anim3 by infiniteTransition.animateFloat(
        initialValue = 0.15f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(530, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "bar3"
    )

    Row(
        modifier = modifier.height(maxHeight),
        horizontalArrangement = Arrangement.spacedBy(2.5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (i in 0 until barCount) {
            val scale = if (isPlaying) {
                when (i % 3) {
                    0 -> anim1 * (0.6f + (i % 5) * 0.08f)
                    1 -> anim2 * (0.7f + (i % 4) * 0.07f)
                    else -> anim3 * (0.5f + (i % 6) * 0.09f)
                }.coerceIn(0.15f, 1.0f)
            } else {
                0.2f
            }

            Box(
                modifier = Modifier
                    .width(barWidth)
                    .fillMaxHeight(scale)
                    .clip(RoundedCornerShape(2.dp))
                    .background(barColor.copy(alpha = if (isPlaying) 0.9f else 0.35f))
            )
        }
    }
}
