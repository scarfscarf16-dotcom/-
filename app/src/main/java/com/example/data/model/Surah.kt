package com.example.data.model

data class Surah(
    val id: Int,
    val nameArabic: String,
    val nameTransliteration: String,
    val nameEnglish: String,
    val versesCount: Int,
    val revelationType: RevelationType,
    val orderInMushaf: Int = id
)

enum class RevelationType(val arabicLabel: String) {
    MECCAN("مكية"),
    MEDINAN("مدنية")
}

data class Ayah(
    val surahNumber: Int,
    val ayahNumber: Int,
    val textArabic: String,
    val translationEnglish: String = "",
    val translationArabicSimplified: String = "",
    val estimatedDurationSeconds: Float = 5.0f
) {
    val globalIdentifier: String
        get() = "${surahNumber}_${ayahNumber}"

    val formattedAyahNumber: String
        get() = ayahNumber.toArabicDigits()
}

fun Int.toArabicDigits(): String {
    val arabicNumerals = arrayOf('٠', '١', '٢', '٣', '٤', '٥', '٦', '٧', '٨', '٩')
    val sb = StringBuilder()
    val str = this.toString()
    for (char in str) {
        if (char in '0'..'9') {
            sb.append(arabicNumerals[char - '0'])
        } else {
            sb.append(char)
        }
    }
    return sb.toString()
}
