package com.example.data.model

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import com.example.R

enum class BackgroundType {
    IMAGE_DRAWABLE,
    GRADIENT_ANIMATED
}

enum class ParticleEffectType(val labelArabic: String) {
    GOLD_DUST("غبار الذهب المتطاير"),
    TWINKLING_STARS("نجوم متلألئة"),
    LIGHT_RAYS("أشعة نور روحانية"),
    NONE("بدون جزيئات")
}

data class BackgroundPreset(
    val id: String,
    val titleArabic: String,
    val descriptionArabic: String,
    val type: BackgroundType,
    @DrawableRes val drawableResId: Int? = null,
    val gradientColors: List<Color> = emptyList(),
    val defaultParticle: ParticleEffectType = ParticleEffectType.GOLD_DUST,
    val overlayAlpha: Float = 0.45f
) {
    companion object {
        val ALL_PRESETS = listOf(
            BackgroundPreset(
                id = "mosque_night",
                titleArabic = "أقواس المسجد الشريف",
                descriptionArabic = "أنوار المساجد والمنارات في ليلة هادئة",
                type = BackgroundType.IMAGE_DRAWABLE,
                drawableResId = R.drawable.img_bg_mosque,
                defaultParticle = ParticleEffectType.TWINKLING_STARS,
                overlayAlpha = 0.40f
            ),
            BackgroundPreset(
                id = "islamic_geometry",
                titleArabic = "الزخرفة الإسلامية الملكية",
                descriptionArabic = "نقوش أرابيسك ذهبية على خلفية زمردية فاخرة",
                type = BackgroundType.IMAGE_DRAWABLE,
                drawableResId = R.drawable.img_bg_geometry,
                defaultParticle = ParticleEffectType.GOLD_DUST,
                overlayAlpha = 0.35f
            ),
            BackgroundPreset(
                id = "serene_nature",
                titleArabic = "بحر السكينة والنجوم",
                descriptionArabic = "أمواج هادئة تحت ضوء القمر والسماء الصافية",
                type = BackgroundType.IMAGE_DRAWABLE,
                drawableResId = R.drawable.img_bg_nature,
                defaultParticle = ParticleEffectType.LIGHT_RAYS,
                overlayAlpha = 0.40f
            ),
            BackgroundPreset(
                id = "kaaba_obsidian",
                titleArabic = "مهابة الكعبة المشرفة",
                descriptionArabic = "سواد ملكي عميق محاط بنفحات ذهبية عريقة",
                type = BackgroundType.GRADIENT_ANIMATED,
                gradientColors = listOf(Color(0xFF0F1216), Color(0xFF08090A), Color(0xFF261D0C)),
                defaultParticle = ParticleEffectType.GOLD_DUST,
                overlayAlpha = 0.20f
            ),
            BackgroundPreset(
                id = "emerald_garden",
                titleArabic = "رياض الجنة الخضراء",
                descriptionArabic = "درجات الزمرد النبوي العميق مع بريق الذهب",
                type = BackgroundType.GRADIENT_ANIMATED,
                gradientColors = listOf(Color(0xFF062A1F), Color(0xFF0C1914), Color(0xFF144D3A)),
                defaultParticle = ParticleEffectType.LIGHT_RAYS,
                overlayAlpha = 0.25f
            ),
            BackgroundPreset(
                id = "celestial_sky",
                titleArabic = "سماء الكون والتسبيح",
                descriptionArabic = "أعماق الليل الأزرق مع النجوم البراقة",
                type = BackgroundType.GRADIENT_ANIMATED,
                gradientColors = listOf(Color(0xFF0B1B2B), Color(0xFF050E18), Color(0xFF1C2F45)),
                defaultParticle = ParticleEffectType.TWINKLING_STARS,
                overlayAlpha = 0.30f
            ),
            BackgroundPreset(
                id = "fajr_glow",
                titleArabic = "نور الصباح والفجر",
                descriptionArabic = "أشعة الفجر الذهبية الدافئة تبعث الأمل والسكينة",
                type = BackgroundType.GRADIENT_ANIMATED,
                gradientColors = listOf(Color(0xFF33200D), Color(0xFF190F06), Color(0xFF5A3912)),
                defaultParticle = ParticleEffectType.GOLD_DUST,
                overlayAlpha = 0.25f
            )
        )
    }
}
