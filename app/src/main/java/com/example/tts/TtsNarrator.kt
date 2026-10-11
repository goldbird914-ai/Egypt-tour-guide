package com.example.tts

import android.content.Context
import android.os.Build
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.data.AppLanguage
import java.util.Locale

class TtsNarrator(context: Context) : TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = null
    var isInitialized by mutableStateOf(false)
        private set

    var isSpeaking by mutableStateOf(false)
        private set

    var currentUtteranceId by mutableStateOf<String?>(null)
        private set

    var speechRate by mutableFloatStateOf(0.95f) // slightly relaxed pace for museum audio guides
        private set

    var currentSpokenRange by mutableStateOf<Pair<Int, Int>?>(null)
        private set

    var lastErrorMessage by mutableStateOf<String?>(null)
        private set

    init {
        tts = TextToSpeech(context.applicationContext, this)
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val arLocale = Locale("ar", "EG")
            val result = tts?.setLanguage(arLocale)

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                // Try generic Arabic or device default
                val genericAr = Locale("ar")
                val fallbackResult = tts?.setLanguage(genericAr)
                if (fallbackResult == TextToSpeech.LANG_MISSING_DATA || fallbackResult == TextToSpeech.LANG_NOT_SUPPORTED) {
                    tts?.setLanguage(Locale.getDefault())
                }
            }

            tts?.setSpeechRate(speechRate)
            tts?.setPitch(1.0f)

            tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                override fun onStart(utteranceId: String?) {
                    isSpeaking = true
                    currentUtteranceId = utteranceId
                    lastErrorMessage = null
                }

                override fun onDone(utteranceId: String?) {
                    isSpeaking = false
                    currentUtteranceId = null
                    currentSpokenRange = null
                }

                override fun onError(utteranceId: String?) {
                    isSpeaking = false
                    currentUtteranceId = null
                    lastErrorMessage = "حدث خطأ أثناء نطق النص الصوتي"
                }

                override fun onRangeStart(utteranceId: String?, start: Int, end: Int, frame: Int) {
                    currentSpokenRange = Pair(start, end)
                }
            })

            isInitialized = true
        } else {
            lastErrorMessage = "تعذر تهيئة محرك تحويل النص إلى كلام (TTS)"
        }
    }

    fun speak(text: String, lang: AppLanguage = AppLanguage.ARABIC, utteranceId: String = "narration_${System.currentTimeMillis()}") {
        if (!isInitialized) return
        stop()
        val result = tts?.setLanguage(lang.locale)
        if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
            // Fallback to generic language or US English
            val genericLocale = Locale(lang.code)
            val fallbackResult = tts?.setLanguage(genericLocale)
            if (fallbackResult == TextToSpeech.LANG_MISSING_DATA || fallbackResult == TextToSpeech.LANG_NOT_SUPPORTED) {
                tts?.setLanguage(Locale.US)
            }
        }
        tts?.setSpeechRate(speechRate)
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, utteranceId)
    }

    fun stop() {
        tts?.stop()
        isSpeaking = false
        currentUtteranceId = null
        currentSpokenRange = null
    }

    fun setRate(rate: Float) {
        speechRate = rate
        tts?.setSpeechRate(rate)
    }

    fun shutdown() {
        tts?.stop()
        tts?.shutdown()
        tts = null
        isInitialized = false
        isSpeaking = false
    }
}
