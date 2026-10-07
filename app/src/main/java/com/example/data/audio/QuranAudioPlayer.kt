package com.example.data.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.example.R
import com.example.data.model.Ayah
import com.example.data.model.Reciter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.FileOutputStream
import java.util.concurrent.TimeUnit

class QuranAudioPlayer(private val context: Context) {

    private val tag = "QuranAudioPlayer"
    private var mediaPlayer: MediaPlayer? = null
    private val mainHandler = Handler(Looper.getMainLooper())
    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    private val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .followRedirects(true)
        .build()

    private var currentAyahs: List<Ayah> = emptyList()
    private var currentReciter: Reciter = Reciter.DEFAULT_RECITERS.first()

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    private val _currentAyahIndex = MutableStateFlow(0)
    val currentAyahIndex: StateFlow<Int> = _currentAyahIndex.asStateFlow()

    private val _currentPositionMs = MutableStateFlow(0)
    val currentPositionMs: StateFlow<Int> = _currentPositionMs.asStateFlow()

    private val _totalDurationMs = MutableStateFlow(0)
    val totalDurationMs: StateFlow<Int> = _totalDurationMs.asStateFlow()

    private val _clipElapsedMs = MutableStateFlow(0)
    val clipElapsedMs: StateFlow<Int> = _clipElapsedMs.asStateFlow()

    private val _clipTotalDurationMs = MutableStateFlow(0)
    val clipTotalDurationMs: StateFlow<Int> = _clipTotalDurationMs.asStateFlow()

    private val _isBuffering = MutableStateFlow(false)
    val isBuffering: StateFlow<Boolean> = _isBuffering.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _volumeLevel = MutableStateFlow(1.0f)
    val volumeLevel: StateFlow<Float> = _volumeLevel.asStateFlow()

    private val _isCachedLocally = MutableStateFlow(false)
    val isCachedLocally: StateFlow<Boolean> = _isCachedLocally.asStateFlow()

    // Cache to hold exact known durations of ayahs in milliseconds
    private val ayahDurationCache = mutableMapOf<Int, Int>()

    private var shouldAutoPlayOnPrepare = false
    private var activeLoadJob: Job? = null
    private var preCacheJob: Job? = null

    private val progressRunnable = object : Runnable {
        override fun run() {
            mediaPlayer?.let { player ->
                try {
                    if (player.isPlaying) {
                        val currentMs = player.currentPosition
                        val durMs = player.duration.coerceAtLeast(1)
                        _currentPositionMs.value = currentMs
                        _totalDurationMs.value = durMs
                        ayahDurationCache[_currentAyahIndex.value] = durMs

                        // Calculate overall clip progress
                        var elapsed = 0
                        for (i in 0 until _currentAyahIndex.value) {
                            elapsed += ayahDurationCache[i] ?: ((currentAyahs.getOrNull(i)?.estimatedDurationSeconds ?: 5f) * 1000).toInt()
                        }
                        elapsed += currentMs
                        _clipElapsedMs.value = elapsed
                    }
                } catch (e: Exception) {
                    Log.w(tag, "Progress check: ${e.message}")
                }
            }
            if (_isPlaying.value) {
                mainHandler.postDelayed(this, 100)
            }
        }
    }

    init {
        ensureAudioUnmuted()
    }

    fun ensureAudioUnmuted() {
        try {
            val maxVol = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
            val currentVol = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
            if (currentVol < (maxVol * 0.8).toInt()) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVol, 0)
            }
        } catch (e: Exception) {
            Log.w(tag, "Audio volume adjustment failed: ${e.message}")
        }
    }

    fun setAppVolume(volume: Float) {
        val safeVol = volume.coerceIn(0f, 1f)
        _volumeLevel.value = safeVol
        ensureAudioUnmuted()
        try {
            mediaPlayer?.setVolume(safeVol, safeVol)
        } catch (e: Exception) {
            Log.w(tag, "Failed to set player volume: ${e.message}")
        }
    }

    /**
     * Prepares the sequence of verses for playback.
     * Computes the total clip duration (which accumulates until >= target duration)
     * and triggers pre-caching so sequential playback is completely seamless.
     */
    fun prepareAyahs(ayahs: List<Ayah>, reciter: Reciter, startIndex: Int = 0, autoPlay: Boolean = false) {
        stop()
        if (ayahs.isEmpty()) return

        currentAyahs = ayahs
        currentReciter = reciter
        _currentAyahIndex.value = startIndex.coerceIn(0, ayahs.size - 1)
        _errorMessage.value = null
        shouldAutoPlayOnPrepare = autoPlay

        // Calculate clip total duration (sum of estimated seconds in ms)
        val estimatedTotalMs = (ayahs.sumOf { it.estimatedDurationSeconds.toDouble() } * 1000).toInt()
        _clipTotalDurationMs.value = estimatedTotalMs
        _clipElapsedMs.value = 0

        // Pre-cache all ayahs of this clip in the background
        preCacheAllAyahsInClip(ayahs, reciter)

        loadAndPlayCurrentAyah()
    }

    private fun preCacheAllAyahsInClip(ayahs: List<Ayah>, reciter: Reciter) {
        preCacheJob?.cancel()
        preCacheJob = scope.launch(Dispatchers.IO) {
            for (ayah in ayahs) {
                try {
                    fetchOrGetCachedAudioFile(reciter, ayah.surahNumber, ayah.ayahNumber)
                } catch (e: Exception) {
                    Log.w(tag, "Pre-cache item failed: ${ayah.ayahNumber}: ${e.message}")
                }
            }
        }
    }

    fun togglePlayPause() {
        if (_isPlaying.value) {
            pause()
        } else {
            resume()
        }
    }

    fun pause() {
        shouldAutoPlayOnPrepare = false
        try {
            mediaPlayer?.let { player ->
                if (player.isPlaying) {
                    player.pause()
                }
            }
        } catch (e: Exception) {
            Log.e(tag, "Pause error: ${e.message}")
        }
        _isPlaying.value = false
        mainHandler.removeCallbacks(progressRunnable)
    }

    fun resume() {
        if (currentAyahs.isEmpty()) return
        ensureAudioUnmuted()
        requestAudioFocus()

        try {
            mediaPlayer?.let { player ->
                player.setVolume(_volumeLevel.value, _volumeLevel.value)
                player.start()
                _isPlaying.value = true
                mainHandler.post(progressRunnable)
            } ?: run {
                shouldAutoPlayOnPrepare = true
                loadAndPlayCurrentAyah()
            }
        } catch (e: Exception) {
            Log.e(tag, "Resume error: ${e.message}")
            shouldAutoPlayOnPrepare = true
            loadAndPlayCurrentAyah()
        }
    }

    fun skipToAyah(index: Int) {
        if (index in currentAyahs.indices) {
            _currentAyahIndex.value = index
            shouldAutoPlayOnPrepare = true
            loadAndPlayCurrentAyah()
        }
    }

    fun nextAyah() {
        val next = _currentAyahIndex.value + 1
        if (next < currentAyahs.size) {
            skipToAyah(next)
        } else {
            // Reached end of clip: loop back to beginning
            _currentAyahIndex.value = 0
            _currentPositionMs.value = 0
            _clipElapsedMs.value = 0
            pause()
        }
    }

    fun previousAyah() {
        val prev = _currentAyahIndex.value - 1
        if (prev >= 0) {
            skipToAyah(prev)
        }
    }

    fun seekTo(positionMs: Int) {
        try {
            mediaPlayer?.seekTo(positionMs)
            _currentPositionMs.value = positionMs
        } catch (e: Exception) {
            Log.w(tag, "Seek error: ${e.message}")
        }
    }

    private fun loadAndPlayCurrentAyah() {
        val index = _currentAyahIndex.value
        if (index !in currentAyahs.indices) return

        val ayah = currentAyahs[index]
        releaseMediaPlayer()
        _isBuffering.value = true
        _errorMessage.value = null

        activeLoadJob?.cancel()
        activeLoadJob = scope.launch {
            // Check if cached on disk
            val cachedFile = withContext(Dispatchers.IO) {
                fetchOrGetCachedAudioFile(currentReciter, ayah.surahNumber, ayah.ayahNumber)
            }

            try {
                ensureAudioUnmuted()
                requestAudioFocus()

                val player = MediaPlayer().apply {
                    setAudioAttributes(
                        AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .setLegacyStreamType(AudioManager.STREAM_MUSIC)
                            .build()
                    )

                    // 1) Primary: play cached file if available
                    if (cachedFile != null && cachedFile.exists() && cachedFile.length() > 2048) {
                        _isCachedLocally.value = true
                        setDataSource(cachedFile.absolutePath)
                    } else {
                        // 2) Secondary: Direct stream from EveryAyah CDN or fallback CDN
                        val directUrl = currentReciter.getAudioUrl(ayah.surahNumber, ayah.ayahNumber)
                        Log.d(tag, "Playing directly from URL: $directUrl")
                        try {
                            setDataSource(directUrl)
                        } catch (e: Exception) {
                            // 3) Tertiary: fallback to raw resource if all else fails
                            Log.w(tag, "URL streaming failed, using fallback resource: ${e.message}")
                            val afd = context.resources.openRawResourceFd(R.raw.recitation_fallback)
                            setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
                            afd.close()
                        }
                    }

                    setVolume(_volumeLevel.value, _volumeLevel.value)

                    setOnPreparedListener { mp ->
                        _isBuffering.value = false
                        val dur = mp.duration
                        _totalDurationMs.value = dur
                        _currentPositionMs.value = 0
                        ayahDurationCache[index] = dur
                        mp.setVolume(_volumeLevel.value, _volumeLevel.value)

                        if (shouldAutoPlayOnPrepare) {
                            mp.start()
                            _isPlaying.value = true
                            mainHandler.post(progressRunnable)
                        } else {
                            _isPlaying.value = false
                        }
                    }

                    setOnCompletionListener {
                        onAyahPlaybackFinished()
                    }

                    setOnErrorListener { mp, what, extra ->
                        Log.e(tag, "MediaPlayer error ($what, $extra)")
                        _isBuffering.value = false
                        _isPlaying.value = false
                        // Fallback to raw resource to guarantee sound
                        try {
                            mp.reset()
                            val afd = context.resources.openRawResourceFd(R.raw.recitation_fallback)
                            mp.setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
                            afd.close()
                            mp.prepare()
                            mp.start()
                            _isPlaying.value = true
                            mainHandler.post(progressRunnable)
                        } catch (ex: Exception) {
                            _errorMessage.value = "جاري إعادة الاتصال بسيرفر التلاوة..."
                        }
                        true
                    }
                }

                mediaPlayer = player
                player.prepareAsync()

            } catch (e: Exception) {
                Log.e(tag, "Player init failed: ${e.message}", e)
                _isBuffering.value = false
                _errorMessage.value = "خطأ في تهيئة مشغل الصوت: ${e.message}"
            }
        }
    }

    private fun fetchOrGetCachedAudioFile(reciter: Reciter, surah: Int, ayah: Int): File? {
        val cacheDir = File(context.cacheDir, "quran_audio_cache")
        if (!cacheDir.exists()) cacheDir.mkdirs()

        val fileName = "${reciter.id}_${surah}_$ayah.mp3"
        val localFile = File(cacheDir, fileName)

        if (localFile.exists() && localFile.length() > 4096) {
            return localFile
        }

        val primaryUrl = reciter.getAudioUrl(surah, ayah)
        val globalIndex = getGlobalAyahIndex(surah, ayah)
        val fallbackUrl = "https://cdn.islamic.network/quran/audio/128/ar.alafasy/$globalIndex.mp3"
        val secondaryUrl = "https://cdn.islamic.network/quran/audio/64/ar.alafasy/$globalIndex.mp3"

        val urls = listOf(primaryUrl, fallbackUrl, secondaryUrl)

        for (url in urls) {
            try {
                val request = Request.Builder()
                    .url(url)
                    .addHeader("User-Agent", "Mozilla/5.0 Android QuranAudio/1.0")
                    .build()

                val response = okHttpClient.newCall(request).execute()
                if (response.isSuccessful && response.body != null) {
                    val bytes = response.body!!.bytes()
                    if (bytes.size > 2000) {
                        val tempFile = File(cacheDir, "${fileName}.tmp")
                        FileOutputStream(tempFile).use { fos ->
                            fos.write(bytes)
                        }
                        if (tempFile.renameTo(localFile) || tempFile.copyTo(localFile, overwrite = true).exists()) {
                            tempFile.delete()
                            Log.d(tag, "Cached $fileName (${localFile.length()} bytes)")
                            return localFile
                        }
                    }
                }
            } catch (e: Exception) {
                Log.w(tag, "Fetch error from $url: ${e.message}")
            }
        }

        return if (localFile.exists() && localFile.length() > 1000) localFile else null
    }

    private fun getGlobalAyahIndex(surah: Int, ayah: Int): Int {
        var total = 0
        val surahCounts = listOf(
            7, 286, 200, 176, 120, 165, 206, 75, 129, 109, 123, 111, 43, 52, 99, 128, 111, 110, 98, 135,
            112, 78, 118, 64, 77, 227, 93, 88, 69, 60, 34, 30, 73, 54, 45, 83, 182, 88, 75, 85,
            54, 53, 89, 59, 37, 35, 38, 29, 18, 45, 60, 49, 62, 55, 78, 96, 29, 22, 24, 13,
            14, 11, 11, 18, 12, 12, 30, 52, 52, 44, 28, 28, 20, 56, 40, 31, 50, 40, 46, 42,
            29, 19, 36, 25, 22, 17, 19, 26, 30, 20, 15, 21, 11, 8, 8, 19, 5, 8, 8, 11,
            11, 8, 3, 9, 5, 4, 7, 3, 6, 3, 5, 4, 5, 6
        )
        for (i in 0 until (surah - 1).coerceIn(0, surahCounts.size - 1)) {
            total += surahCounts[i]
        }
        return total + ayah
    }

    private fun requestAudioFocus() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val focusRequest = AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
                    .setAudioAttributes(
                        AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .build()
                    )
                    .build()
                audioManager.requestAudioFocus(focusRequest)
            } else {
                @Suppress("DEPRECATION")
                audioManager.requestAudioFocus(null, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN)
            }
        } catch (e: Exception) {
            Log.w(tag, "Audio focus failed: ${e.message}")
        }
    }

    /**
     * Seamless Chaining:
     * When current Ayah finishes, immediately advances to next Ayah!
     */
    private fun onAyahPlaybackFinished() {
        mainHandler.removeCallbacks(progressRunnable)
        val nextIndex = _currentAyahIndex.value + 1
        if (nextIndex < currentAyahs.size) {
            _currentAyahIndex.value = nextIndex
            shouldAutoPlayOnPrepare = true
            loadAndPlayCurrentAyah()
        } else {
            // Completed all verses in the clip! Loop back to start smoothly
            _isPlaying.value = false
            _currentPositionMs.value = 0
            _clipElapsedMs.value = 0
            _currentAyahIndex.value = 0
            shouldAutoPlayOnPrepare = false
        }
    }

    private fun releaseMediaPlayer() {
        mainHandler.removeCallbacks(progressRunnable)
        try {
            mediaPlayer?.apply {
                if (isPlaying) {
                    stop()
                }
                reset()
                release()
            }
        } catch (e: Exception) {
            Log.w(tag, "Release error: ${e.message}")
        }
        mediaPlayer = null
    }

    fun stop() {
        activeLoadJob?.cancel()
        releaseMediaPlayer()
        _isPlaying.value = false
        _isBuffering.value = false
        _currentPositionMs.value = 0
        _clipElapsedMs.value = 0
    }
}
