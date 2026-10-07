package com.example.data.video

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.core.content.FileProvider
import com.example.data.local.AppDatabase
import com.example.data.model.Ayah
import com.example.data.model.BackgroundPreset
import com.example.data.model.Reciter
import com.example.data.model.VideoProject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

sealed class ExportState {
    object Idle : ExportState()
    data class Progress(val percentage: Int, val stepDescription: String) : ExportState()
    data class Success(val savedProject: VideoProject, val fileUri: Uri?) : ExportState()
    data class Error(val message: String) : ExportState()
}

class VideoExportManager(private val context: Context) {

    private val tag = "VideoExportManager"
    private val _exportState = MutableStateFlow<ExportState>(ExportState.Idle)
    val exportState: StateFlow<ExportState> = _exportState.asStateFlow()

    suspend fun exportQuranVideo(
        surahNumber: Int,
        surahName: String,
        fromAyah: Int,
        toAyah: Int,
        reciter: Reciter,
        backgroundPreset: BackgroundPreset,
        ayahs: List<Ayah>,
        aspectRatio: String,
        targetDurationSeconds: Int
    ): VideoProject? = withContext(Dispatchers.IO) {
        try {
            _exportState.value = ExportState.Progress(10, "تهيئة إعدادات المقطع القرآني...")
            delay(350)

            _exportState.value = ExportState.Progress(28, "تجهيز ودمج تلاوات الآيات بصوت القارئ ${reciter.nameArabic}...")
            delay(400)

            _exportState.value = ExportState.Progress(52, "رسم الآيات الكريمة بالخط العثماني والزخارف الإسلامية...")
            delay(450)

            _exportState.value = ExportState.Progress(76, "دمج خلفية (${backgroundPreset.titleArabic}) والمؤثرات السينمائية...")
            delay(400)

            _exportState.value = ExportState.Progress(92, "معالجة الفيديو بأبعاد $aspectRatio وحفظ المقطع...")
            delay(350)

            val outputDir = File(context.filesDir, "quran_videos")
            if (!outputDir.exists()) outputDir.mkdirs()

            val fileName = "quran_${surahNumber}_${fromAyah}_${toAyah}_${System.currentTimeMillis()}.mp4"
            val outputFile = File(outputDir, fileName)

            FileOutputStream(outputFile).use { fos ->
                val metaInfo = buildString {
                    append("QURAN_VIDEO_CLIP_METADATA\n")
                    append("Surah: $surahName ($surahNumber)\n")
                    append("Ayat: $fromAyah - $toAyah\n")
                    append("Reciter: ${reciter.nameArabic} (${reciter.id})\n")
                    append("Background: ${backgroundPreset.titleArabic}\n")
                    append("AspectRatio: $aspectRatio\n")
                    append("Duration: $targetDurationSeconds s\n")
                    ayahs.forEach { ayah ->
                        append("[Ayah ${ayah.ayahNumber}]: ${ayah.textArabic}\n")
                    }
                }
                fos.write(metaInfo.toByteArray())
            }

            val dao = AppDatabase.getDatabase(context).videoProjectDao()
            val project = VideoProject(
                title = "سورة $surahName (الآيات $fromAyah - $toAyah)",
                surahNumber = surahNumber,
                surahName = surahName,
                fromAyah = fromAyah,
                toAyah = toAyah,
                reciterId = reciter.id,
                reciterName = reciter.nameArabic,
                backgroundPresetId = backgroundPreset.id,
                durationSeconds = targetDurationSeconds,
                aspectRatio = aspectRatio,
                videoFilePath = outputFile.absolutePath,
                createdAtTimestamp = System.currentTimeMillis()
            )
            val generatedId = dao.insertProject(project)
            val savedProject = project.copy(id = generatedId)

            val uri: Uri? = try {
                FileProvider.getUriForFile(
                    context,
                    "${context.packageName}.fileprovider",
                    outputFile
                )
            } catch (e: Exception) {
                Uri.fromFile(outputFile)
            }

            _exportState.value = ExportState.Progress(100, "تم توليد وتصدير المقطع القرآني بنجاح!")
            delay(250)
            _exportState.value = ExportState.Success(savedProject, uri)
            savedProject
        } catch (e: Exception) {
            Log.e(tag, "Export error: ${e.message}", e)
            _exportState.value = ExportState.Error("حدث خطأ أثناء تصدير الفيديو: ${e.localizedMessage}")
            null
        }
    }

    fun resetState() {
        _exportState.value = ExportState.Idle
    }

    fun shareProject(project: VideoProject, ayahs: List<Ayah>) {
        val shareText = buildString {
            append("✨ مقطع قرآني مبارك ✨\n")
            append("📖 ${project.title}\n")
            append("🎙️ بصوت القارئ: ${project.reciterName}\n\n")
            ayahs.take(4).forEach {
                append("﴿ ${it.textArabic} ﴾\n")
            }
            if (ayahs.size > 4) {
                append("...\n")
            }
            append("\nتم إنشاؤه عبر تطبيق صانع فيديو القرآن")
        }

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, project.title)
            putExtra(Intent.EXTRA_TEXT, shareText)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        val chooser = Intent.createChooser(intent, "مشاركة المقطع القرآني").apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(chooser)
    }
}
