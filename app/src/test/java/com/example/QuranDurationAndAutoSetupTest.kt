package com.example

import com.example.data.model.Reciter
import com.example.data.repository.QuranRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class QuranDurationAndAutoSetupTest {

    @Test
    fun testDurationAggregationReachesOrExceedsTargetWithoutCutting() {
        // User rule:
        // "يمكن لمدة فيديو هي تلاوات ايات بثواني تدمج بترتيب حتى يصل اخر جزء منها مجموع 60 او اكثر . لانه لا يجوز تقطيع ايه في نصف تلاوه فلا باس بزيادة بضع ثواني"
        val surahNumber = 59 // Al-Hashr
        val startAyah = 21
        val targetSeconds = 60

        val (from, to) = QuranRepository.calculateAyahRangeForTargetDuration(
            surahNumber = surahNumber,
            startAyah = startAyah,
            targetDurationSeconds = targetSeconds
        )

        assertEquals(21, from)
        assertTrue("End ayah must be at least start ayah", to >= from)

        val ayahs = QuranRepository.getAyahsForRange(surahNumber, from, to)
        val totalSec = ayahs.sumOf { it.estimatedDurationSeconds.toDouble() }

        // Must reach or exceed 60 seconds completely
        assertTrue(
            "Total duration ($totalSec) must be >= target ($targetSeconds) to complete final ayah",
            totalSec >= targetSeconds
        )
    }

    @Test
    fun testGenerateAutoVideoConfigurationFor60Seconds() {
        // User rule:
        // "و اضف خاصية انشاء اعدادات فيديو بشكل تلقائي"
        val config = QuranRepository.generateAutoVideoConfiguration(targetDuration = 60)

        assertNotNull("Surah must not be null", config.surah)
        assertNotNull("Reciter must not be null", config.reciter)
        assertNotNull("Background must not be null", config.backgroundPreset)
        assertEquals("Aspect ratio should default to 9:16", "9:16", config.aspectRatio)

        assertTrue(
            "Auto config actual duration (${config.actualDurationSeconds}) must be >= 60 to finish full final verse",
            config.actualDurationSeconds >= 60
        )

        val ayahs = QuranRepository.getAyahsForRange(config.surah.id, config.fromAyah, config.toAyah)
        assertTrue("Must contain at least one ayah", ayahs.isNotEmpty())
    }

    @Test
    fun testReciterAudioUrlFormatting() {
        val reciter = Reciter.DEFAULT_RECITERS.first()
        val url = reciter.getAudioUrl(1, 1)
        assertEquals("https://everyayah.com/data/Alafasy_128kbps/001001.mp3", url)
    }
}
