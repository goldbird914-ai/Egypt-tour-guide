package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.Subtitles
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AppLanguage
import com.example.data.AppStrings
import com.example.tts.TtsNarrator
import com.example.ui.theme.GoldDark
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.NileTurquoise

@Composable
fun InteractiveAudioPlayer(
    title: String,
    totalSeconds: Int,
    scriptText: String,
    detailedHistoryText: String? = null,
    lang: AppLanguage = AppLanguage.ARABIC,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val ttsNarrator = remember { TtsNarrator(context) }

    DisposableEffect(Unit) {
        onDispose {
            ttsNarrator.shutdown()
        }
    }

    var readModeDetailed by remember { mutableStateOf(false) }
    var showScript by remember { mutableStateOf(true) }

    val activeTextToRead = if (readModeDetailed && detailedHistoryText != null) {
        detailedHistoryText
    } else {
        scriptText
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("interactive_audio_player"),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        shape = RoundedCornerShape(18.dp),
        border = CardDefaults.outlinedCardBorder()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header with TTS Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(GoldPrimary.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.RecordVoiceOver,
                            contentDescription = "TTS Voice",
                            tint = GoldPrimary,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = AppStrings.audioPlayerHeader(lang),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                color = if (ttsNarrator.isSpeaking) NileTurquoise else GoldPrimary.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = if (ttsNarrator.isSpeaking) AppStrings.ttsSpeakingBadge(lang) else AppStrings.ttsAvailableBadge(lang),
                                    color = if (ttsNarrator.isSpeaking) Color.White else GoldPrimary,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                        Text(
                            text = title,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                IconButton(
                    onClick = { showScript = !showScript },
                    modifier = Modifier.testTag("toggle_audio_script")
                ) {
                    Icon(
                        imageVector = Icons.Default.Subtitles,
                        contentDescription = "Script",
                        tint = if (showScript) GoldPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Text Source Selector: Brief Audio Script vs Complete Historical Detail
            if (detailedHistoryText != null) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = !readModeDetailed,
                        onClick = {
                            if (readModeDetailed) {
                                readModeDetailed = false
                                if (ttsNarrator.isSpeaking) {
                                    ttsNarrator.speak(scriptText, lang)
                                }
                            }
                        },
                        label = { Text(AppStrings.chipBriefAudio(lang), fontSize = 11.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = GoldPrimary,
                            selectedLabelColor = Color(0xFF1A130B)
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("chip_audio_brief")
                    )

                    FilterChip(
                        selected = readModeDetailed,
                        onClick = {
                            if (!readModeDetailed) {
                                readModeDetailed = true
                                if (ttsNarrator.isSpeaking) {
                                    ttsNarrator.speak(detailedHistoryText, lang)
                                }
                            }
                        },
                        label = { Text(AppStrings.chipDetailedAudio(lang), fontSize = 11.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = GoldPrimary,
                            selectedLabelColor = Color(0xFF1A130B)
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("chip_audio_detailed")
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
            }

            // Visualizer & Controls Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Speech Rate Selector
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Speed,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(16.dp)
                    )
                    listOf(
                        0.85f to (if (lang == AppLanguage.ARABIC) "0.8x هادئ" else "0.8x Museum"),
                        1.0f to "1.0x",
                        1.25f to "1.2x"
                    ).forEach { (rate, label) ->
                        val isSelected = kotlin.math.abs(ttsNarrator.speechRate - rate) < 0.05f
                        Surface(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { ttsNarrator.setRate(rate) }
                                .testTag("rate_btn_$label"),
                            color = if (isSelected) GoldPrimary.copy(alpha = 0.25f) else Color.Transparent,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = label,
                                fontSize = 10.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) GoldPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                            )
                        }
                    }
                }

                // Audio Equalizer Waveform Animation
                AudioEqualizerWaveform(isSpeaking = ttsNarrator.isSpeaking)
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Main Play/Stop Controls
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (ttsNarrator.isSpeaking) {
                    // Stop Button
                    IconButton(
                        onClick = {
                            ttsNarrator.stop()
                        },
                        modifier = Modifier
                            .size(52.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFE74C3C))
                            .testTag("stop_audio_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Stop,
                            contentDescription = "Stop",
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                } else {
                    // Play Button
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(24.dp))
                            .background(GoldPrimary)
                            .clickable {
                                ttsNarrator.speak(activeTextToRead, lang)
                            }
                            .padding(horizontal = 24.dp, vertical = 10.dp)
                            .testTag("play_pause_audio"),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = AppStrings.listenAloudButton(lang),
                            tint = Color(0xFF1A130B),
                            modifier = Modifier.size(26.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = AppStrings.listenAloudButton(lang),
                            color = Color(0xFF1A130B),
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }
            }

            // Expandable/Highlightable Spoken Text Script
            AnimatedVisibility(visible = showScript) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp)
                        .background(
                            MaterialTheme.colorScheme.surface,
                            RoundedCornerShape(10.dp)
                        )
                        .padding(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (readModeDetailed) {
                                if (lang == AppLanguage.ARABIC) "نص السرد التاريخي المفصل:" else "Full Historic Narrative:"
                            } else {
                                if (lang == AppLanguage.ARABIC) "نص المرشد الصوتي:" else "Audio Guide Script:"
                            },
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = GoldDark
                        )
                        if (ttsNarrator.isSpeaking) {
                            Text(
                                text = AppStrings.readingAloudText(lang),
                                style = MaterialTheme.typography.labelSmall,
                                color = NileTurquoise,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    val range = ttsNarrator.currentSpokenRange
                    val annotatedScript = buildAnnotatedString {
                        if (range != null && range.first >= 0 && range.second <= activeTextToRead.length) {
                            val before = activeTextToRead.substring(0, range.first)
                            val current = activeTextToRead.substring(range.first, range.second)
                            val after = activeTextToRead.substring(range.second)

                            append(before)
                            withStyle(
                                SpanStyle(
                                    background = GoldPrimary.copy(alpha = 0.35f),
                                    fontWeight = FontWeight.Bold,
                                    color = GoldPrimary
                                )
                            ) {
                                append(current)
                            }
                            append(after)
                        } else {
                            append(activeTextToRead)
                        }
                    }

                    Text(
                        text = annotatedScript,
                        style = MaterialTheme.typography.bodyMedium,
                        lineHeight = 24.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}

@Composable
private fun AudioEqualizerWaveform(
    isSpeaking: Boolean,
    modifier: Modifier = Modifier
) {
    val transition = rememberInfiniteTransition(label = "waveform_anim")

    val h1 by transition.animateFloat(
        initialValue = 4f,
        targetValue = 20f,
        animationSpec = infiniteRepeatable(tween(400, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "h1"
    )
    val h2 by transition.animateFloat(
        initialValue = 18f,
        targetValue = 6f,
        animationSpec = infiniteRepeatable(tween(350, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "h2"
    )
    val h3 by transition.animateFloat(
        initialValue = 8f,
        targetValue = 22f,
        animationSpec = infiniteRepeatable(tween(450, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "h3"
    )
    val h4 by transition.animateFloat(
        initialValue = 16f,
        targetValue = 5f,
        animationSpec = infiniteRepeatable(tween(320, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "h4"
    )

    Row(
        modifier = modifier.height(26.dp),
        horizontalArrangement = Arrangement.spacedBy(3.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        listOf(h1, h2, h3, h4).forEach { animatedHeight ->
            val height = if (isSpeaking) animatedHeight.dp else 4.dp
            Box(
                modifier = Modifier
                    .width(3.dp)
                    .height(height)
                    .clip(CircleShape)
                    .background(if (isSpeaking) GoldPrimary else MaterialTheme.colorScheme.outline.copy(alpha = 0.4f))
            )
        }
    }
}
