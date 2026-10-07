package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.audio.QuranAudioPlayer
import com.example.data.local.AppDatabase
import com.example.data.model.Ayah
import com.example.data.model.BackgroundPreset
import com.example.data.model.ParticleEffectType
import com.example.data.model.Reciter
import com.example.data.model.Surah
import com.example.data.model.VideoProject
import com.example.data.repository.QuranRepository
import com.example.data.repository.RecommendedClip
import com.example.data.video.ExportState
import com.example.data.video.VideoExportManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class QuranStudioViewModel(application: Application) : AndroidViewModel(application) {

    private val audioPlayer = QuranAudioPlayer(application.applicationContext)
    private val exportManager = VideoExportManager(application.applicationContext)
    private val projectDao = AppDatabase.getDatabase(application.applicationContext).videoProjectDao()

    val isPlaying: StateFlow<Boolean> = audioPlayer.isPlaying
    val currentAyahIndex: StateFlow<Int> = audioPlayer.currentAyahIndex
    val currentPositionMs: StateFlow<Int> = audioPlayer.currentPositionMs
    val totalDurationMs: StateFlow<Int> = audioPlayer.totalDurationMs
    val clipElapsedMs: StateFlow<Int> = audioPlayer.clipElapsedMs
    val clipTotalDurationMs: StateFlow<Int> = audioPlayer.clipTotalDurationMs
    val isBuffering: StateFlow<Boolean> = audioPlayer.isBuffering
    val isCachedLocally: StateFlow<Boolean> = audioPlayer.isCachedLocally
    val exportState: StateFlow<ExportState> = exportManager.exportState
    val volumeLevel: StateFlow<Float> = audioPlayer.volumeLevel
    val audioErrorMessage: StateFlow<String?> = audioPlayer.errorMessage

    val savedProjects: StateFlow<List<VideoProject>> = projectDao.getAllProjects()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    // Default to Surah Al-Hashr (59:21-24) which is ~63 seconds, matching user's 60s requirement
    private val _currentSurah = MutableStateFlow(QuranRepository.getSurahById(59) ?: QuranRepository.ALL_SURAHS[0])
    val currentSurah: StateFlow<Surah> = _currentSurah.asStateFlow()

    private val _fromAyah = MutableStateFlow(21)
    val fromAyah: StateFlow<Int> = _fromAyah.asStateFlow()

    private val _toAyah = MutableStateFlow(24)
    val toAyah: StateFlow<Int> = _toAyah.asStateFlow()

    private val _currentAyahs = MutableStateFlow<List<Ayah>>(emptyList())
    val currentAyahs: StateFlow<List<Ayah>> = _currentAyahs.asStateFlow()

    // 60 Seconds is the default target requested by user
    private val _targetDurationSeconds = MutableStateFlow(60)
    val targetDurationSeconds: StateFlow<Int> = _targetDurationSeconds.asStateFlow()

    private val _actualDurationSeconds = MutableStateFlow(63)
    val actualDurationSeconds: StateFlow<Int> = _actualDurationSeconds.asStateFlow()

    private val _currentReciter = MutableStateFlow(Reciter.DEFAULT_RECITERS.first())
    val currentReciter: StateFlow<Reciter> = _currentReciter.asStateFlow()

    private val _currentBackgroundPreset = MutableStateFlow(BackgroundPreset.ALL_PRESETS.first())
    val currentBackgroundPreset: StateFlow<BackgroundPreset> = _currentBackgroundPreset.asStateFlow()

    private val _currentParticleType = MutableStateFlow(ParticleEffectType.GOLD_DUST)
    val currentParticleType: StateFlow<ParticleEffectType> = _currentParticleType.asStateFlow()

    private val _currentAspectRatio = MutableStateFlow("9:16")
    val currentAspectRatio: StateFlow<String> = _currentAspectRatio.asStateFlow()

    private val _showTranslation = MutableStateFlow(true)
    val showTranslation: StateFlow<Boolean> = _showTranslation.asStateFlow()

    private val _showBasmala = MutableStateFlow(true)
    val showBasmala: StateFlow<Boolean> = _showBasmala.asStateFlow()

    private val _autoSetupBanner = MutableStateFlow<String?>(null)
    val autoSetupBanner: StateFlow<String?> = _autoSetupBanner.asStateFlow()

    init {
        unmuteAndMaximizeVolume()
        loadAyahsForRange(_currentSurah.value.id, _fromAyah.value, _toAyah.value, autoPlay = false)
    }

    private fun loadAyahsForRange(surahNumber: Int, from: Int, to: Int, autoPlay: Boolean = false) {
        val ayahs = QuranRepository.getAyahsForRange(surahNumber, from, to)
        _currentAyahs.value = ayahs
        val durationSum = ayahs.sumOf { it.estimatedDurationSeconds.toDouble() }.toInt()
        _actualDurationSeconds.value = durationSum.coerceAtLeast(1)
        audioPlayer.ensureAudioUnmuted()
        audioPlayer.prepareAyahs(ayahs, _currentReciter.value, 0, autoPlay = autoPlay)
    }

    /**
     * User rule:
     * "يمكن لمدة فيديو هي تلاوات ايات بثواني تدمج بترتيب حتى يصل اخر جزء منها مجموع 60 او اكثر . لانه لا يجوز تقطيع ايه في نصف تلاوه فلا باس بزيادة بضع ثواني"
     * Calculates the exact sequence of full verses that reaches or exceeds target duration.
     */
    fun setTargetDuration(durationSeconds: Int) {
        _targetDurationSeconds.value = durationSeconds
        val surah = _currentSurah.value
        val (newFrom, newTo) = QuranRepository.calculateAyahRangeForTargetDuration(
            surahNumber = surah.id,
            startAyah = _fromAyah.value,
            targetDurationSeconds = durationSeconds
        )
        _fromAyah.value = newFrom
        _toAyah.value = newTo
        val wasPlaying = isPlaying.value
        loadAyahsForRange(surah.id, newFrom, newTo, autoPlay = wasPlaying)
    }

    /**
     * FEATURE REQUEST:
     * "و اضف خاصية انشاء اعدادات فيديو بشكل تلقائي"
     * One-Click Instant Auto Setup:
     * Generates a complete harmonious configuration (verses >= 60s without cutting, reciter, background, particle)
     * and immediately plays with audible sound!
     */
    fun createAutoVideoSetup(targetDuration: Int = _targetDurationSeconds.value, autoPlay: Boolean = true) {
        val config = QuranRepository.generateAutoVideoConfiguration(targetDuration = targetDuration)
        _targetDurationSeconds.value = targetDuration
        _currentSurah.value = config.surah
        _fromAyah.value = config.fromAyah
        _toAyah.value = config.toAyah
        _currentReciter.value = config.reciter
        _currentBackgroundPreset.value = config.backgroundPreset
        _currentParticleType.value = config.particleEffect
        _currentAspectRatio.value = config.aspectRatio
        _actualDurationSeconds.value = config.actualDurationSeconds

        unmuteAndMaximizeVolume()
        loadAyahsForRange(config.surah.id, config.fromAyah, config.toAyah, autoPlay = autoPlay)

        _autoSetupBanner.value = "✨ تم إنشاء إعدادات الفيديو تلقائياً: سورة ${config.surah.nameArabic} (الآيات ${config.fromAyah}-${config.toAyah}) بصوت ${config.reciter.nameArabic} (${config.actualDurationSeconds} ثانية تامة دون بتر أي آية)"
    }

    fun dismissAutoSetupBanner() {
        _autoSetupBanner.value = null
    }

    fun selectSurahAndRange(surah: Surah, from: Int, to: Int) {
        _currentSurah.value = surah
        _fromAyah.value = from
        _toAyah.value = to
        val wasPlaying = isPlaying.value
        loadAyahsForRange(surah.id, from, to, autoPlay = wasPlaying)
    }

    fun autoFitDurationForSurah(surah: Surah, startAyah: Int) {
        _currentSurah.value = surah
        val (from, to) = QuranRepository.calculateAyahRangeForTargetDuration(
            surahNumber = surah.id,
            startAyah = startAyah,
            targetDurationSeconds = _targetDurationSeconds.value
        )
        _fromAyah.value = from
        _toAyah.value = to
        val wasPlaying = isPlaying.value
        loadAyahsForRange(surah.id, from, to, autoPlay = wasPlaying)
    }

    fun loadRecommendedClip(clip: RecommendedClip) {
        val surah = QuranRepository.getSurahById(clip.surahNumber) ?: return
        _currentSurah.value = surah
        _fromAyah.value = clip.fromAyah
        _toAyah.value = clip.toAyah
        _targetDurationSeconds.value = clip.durationSeconds
        unmuteAndMaximizeVolume()
        loadAyahsForRange(surah.id, clip.fromAyah, clip.toAyah, autoPlay = true)
    }

    fun setReciter(reciter: Reciter) {
        _currentReciter.value = reciter
        val ayahs = _currentAyahs.value
        val currentIndex = currentAyahIndex.value
        val wasPlaying = isPlaying.value
        audioPlayer.prepareAyahs(ayahs, reciter, currentIndex, autoPlay = wasPlaying)
    }

    fun setBackgroundPreset(preset: BackgroundPreset) {
        _currentBackgroundPreset.value = preset
        _currentParticleType.value = preset.defaultParticle
    }

    fun setParticleType(particleType: ParticleEffectType) {
        _currentParticleType.value = particleType
    }

    fun setAspectRatio(ratio: String) {
        _currentAspectRatio.value = ratio
    }

    fun toggleTranslation() {
        _showTranslation.value = !_showTranslation.value
    }

    fun toggleBasmala() {
        _showBasmala.value = !_showBasmala.value
    }

    fun setVolume(volume: Float) {
        audioPlayer.setAppVolume(volume)
    }

    fun unmuteAndMaximizeVolume() {
        audioPlayer.ensureAudioUnmuted()
        audioPlayer.setAppVolume(1.0f)
    }

    fun togglePlayPause() {
        audioPlayer.ensureAudioUnmuted()
        audioPlayer.togglePlayPause()
    }

    fun skipToNextAyah() {
        audioPlayer.nextAyah()
    }

    fun skipToPreviousAyah() {
        audioPlayer.previousAyah()
    }

    fun seekToAyah(index: Int) {
        audioPlayer.skipToAyah(index)
    }

    fun exportVideo() {
        viewModelScope.launch {
            audioPlayer.pause()
            exportManager.exportQuranVideo(
                surahNumber = _currentSurah.value.id,
                surahName = _currentSurah.value.nameArabic,
                fromAyah = _fromAyah.value,
                toAyah = _toAyah.value,
                reciter = _currentReciter.value,
                backgroundPreset = _currentBackgroundPreset.value,
                ayahs = _currentAyahs.value,
                aspectRatio = _currentAspectRatio.value,
                targetDurationSeconds = _actualDurationSeconds.value
            )
        }
    }

    fun shareExportedVideo() {
        val state = exportManager.exportState.value
        if (state is ExportState.Success) {
            exportManager.shareProject(state.savedProject, _currentAyahs.value)
        }
    }

    fun shareSavedProject(project: VideoProject) {
        val ayahs = QuranRepository.getAyahsForRange(project.surahNumber, project.fromAyah, project.toAyah)
        exportManager.shareProject(project, ayahs)
    }

    fun dismissExportDialog() {
        exportManager.resetState()
    }

    fun deleteProject(id: Long) {
        viewModelScope.launch {
            projectDao.deleteById(id)
        }
    }

    fun toggleFavorite(project: VideoProject) {
        viewModelScope.launch {
            projectDao.updateProject(project.copy(isFavorite = !project.isFavorite))
        }
    }

    override fun onCleared() {
        super.onCleared()
        audioPlayer.stop()
    }
}
