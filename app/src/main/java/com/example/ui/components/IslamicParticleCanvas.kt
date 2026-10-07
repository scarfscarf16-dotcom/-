package com.example.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.example.data.model.ParticleEffectType
import kotlin.random.Random

private data class Particle(
    val initialX: Float,
    val initialY: Float,
    val size: Float,
    val speed: Float,
    val alpha: Float,
    val color: Color
)

@Composable
fun IslamicParticleCanvas(
    particleType: ParticleEffectType,
    modifier: Modifier = Modifier
) {
    if (particleType == ParticleEffectType.NONE) return

    val infiniteTransition = rememberInfiniteTransition(label = "particles")
    val progress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 8000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "particle_motion"
    )

    val particles = remember(particleType) {
        val list = mutableListOf<Particle>()
        val count = when (particleType) {
            ParticleEffectType.GOLD_DUST -> 45
            ParticleEffectType.TWINKLING_STARS -> 55
            ParticleEffectType.LIGHT_RAYS -> 20
            ParticleEffectType.NONE -> 0
        }
        val rnd = Random(42)
        for (i in 0 until count) {
            val color = when (particleType) {
                ParticleEffectType.GOLD_DUST -> Color(0xFFFFD700)
                ParticleEffectType.TWINKLING_STARS -> if (i % 3 == 0) Color(0xFFFFF9E6) else Color(0xFFE0F7FA)
                ParticleEffectType.LIGHT_RAYS -> Color(0xFFFDE68A)
                ParticleEffectType.NONE -> Color.Transparent
            }
            list.add(
                Particle(
                    initialX = rnd.nextFloat(),
                    initialY = rnd.nextFloat(),
                    size = rnd.nextFloat() * 4.5f + 1.5f,
                    speed = rnd.nextFloat() * 0.4f + 0.1f,
                    alpha = rnd.nextFloat() * 0.5f + 0.3f,
                    color = color
                )
            )
        }
        list
    }

    Canvas(modifier = modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height

        when (particleType) {
            ParticleEffectType.GOLD_DUST -> {
                particles.forEach { p ->
                    val y = (p.initialY - (progress * p.speed)) % 1f
                    val currentY = if (y < 0) (y + 1f) * h else y * h
                    val currentX = (p.initialX + kotlin.math.sin(progress * 6.28 + p.initialY * 10) * 0.03f).toFloat() * w
                    val currentAlpha = (p.alpha * (0.4f + 0.6f * kotlin.math.sin(progress * 3.14 + p.initialX * 5).toFloat())).coerceIn(0.1f, 0.9f)

                    drawCircle(
                        color = p.color.copy(alpha = currentAlpha),
                        radius = p.size,
                        center = Offset(currentX, currentY)
                    )
                }
            }
            ParticleEffectType.TWINKLING_STARS -> {
                particles.forEach { p ->
                    val twinkle = (0.2f + 0.8f * kotlin.math.sin(progress * 12.0 + p.initialX * 20).toFloat()).coerceIn(0.1f, 1f)
                    val x = p.initialX * w
                    val y = p.initialY * h
                    drawCircle(
                        color = p.color.copy(alpha = p.alpha * twinkle),
                        radius = p.size * (0.8f + 0.4f * twinkle),
                        center = Offset(x, y)
                    )
                }
            }
            ParticleEffectType.LIGHT_RAYS -> {
                val rayAlpha = (0.08f + 0.04f * kotlin.math.sin(progress * 3.14).toFloat()).coerceIn(0.04f, 0.15f)
                drawRect(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0xFFFFD54F).copy(alpha = rayAlpha * 1.5f),
                            Color(0xFFFFE082).copy(alpha = rayAlpha * 0.6f),
                            Color.Transparent
                        ),
                        center = Offset(w * 0.5f, 0f),
                        radius = h * 0.9f
                    )
                )
            }
            ParticleEffectType.NONE -> Unit
        }
    }
}
