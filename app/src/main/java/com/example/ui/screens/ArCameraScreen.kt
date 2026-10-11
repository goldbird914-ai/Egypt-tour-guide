package com.example.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.FlashOff
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.FlipCameraAndroid
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.ViewInAr
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.example.data.AppLanguage
import com.example.data.AppStrings
import com.example.data.ArArtifact
import com.example.data.ArHotspot
import com.example.data.EgyptTourRepository
import com.example.tts.TtsNarrator
import com.example.ui.components.ArOverlayCanvas
import com.example.ui.components.ArVisualizationMode
import com.example.ui.theme.GoldDark
import com.example.ui.theme.GoldPrimary
import kotlinx.coroutines.delay

@Composable
fun ArCameraScreen(
    lang: AppLanguage = AppLanguage.ARABIC,
    initialArtifactId: String? = null,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val ttsNarrator = remember { TtsNarrator(context) }
    DisposableEffect(Unit) {
        onDispose {
            ttsNarrator.shutdown()
        }
    }

    val allArtifacts = EgyptTourRepository.arArtifacts
    var selectedArtifactId by remember(initialArtifactId) {
        mutableStateOf(initialArtifactId ?: allArtifacts.first().id)
    }

    val currentArtifact = allArtifacts.find { it.id == selectedArtifactId } ?: allArtifacts.first()

    var visualizationMode by remember { mutableStateOf(ArVisualizationMode.RESTORED) }
    var selectedHotspot by remember { mutableStateOf<ArHotspot?>(null) }
    var showSnapshotFlash by remember { mutableStateOf(false) }
    var showSnapshotDialog by remember { mutableStateOf(false) }

    // Camera State
    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    var lensFacing by remember { mutableStateOf(CameraSelector.LENS_FACING_BACK) }
    var isTorchOn by remember { mutableStateOf(false) }
    var activeCamera by remember { mutableStateOf<Camera?>(null) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasCameraPermission = isGranted
    }

    LaunchedEffect(Unit) {
        if (!hasCameraPermission) {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
            .testTag("ar_camera_screen")
    ) {
        // 1. Camera Feed / Camera View
        if (hasCameraPermission) {
            AndroidView(
                factory = { ctx ->
                    val previewView = PreviewView(ctx).apply {
                        scaleType = PreviewView.ScaleType.FILL_CENTER
                    }

                    val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)
                    cameraProviderFuture.addListener({
                        try {
                            val cameraProvider = cameraProviderFuture.get()
                            val preview = Preview.Builder().build().also {
                                it.surfaceProvider = previewView.surfaceProvider
                            }

                            val cameraSelector = CameraSelector.Builder()
                                .requireLensFacing(lensFacing)
                                .build()

                            cameraProvider.unbindAll()
                            activeCamera = cameraProvider.bindToLifecycle(
                                lifecycleOwner,
                                cameraSelector,
                                preview
                            )
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }, ContextCompat.getMainExecutor(ctx))

                    previewView
                },
                modifier = Modifier.fillMaxSize(),
                update = { previewView ->
                    val cameraProviderFuture = ProcessCameraProvider.getInstance(previewView.context)
                    cameraProviderFuture.addListener({
                        try {
                            val cameraProvider = cameraProviderFuture.get()
                            val preview = Preview.Builder().build().also {
                                it.surfaceProvider = previewView.surfaceProvider
                            }

                            val cameraSelector = CameraSelector.Builder()
                                .requireLensFacing(lensFacing)
                                .build()

                            cameraProvider.unbindAll()
                            activeCamera = cameraProvider.bindToLifecycle(
                                lifecycleOwner,
                                cameraSelector,
                                preview
                            )
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }, ContextCompat.getMainExecutor(previewView.context))
                }
            )
        } else {
            CameraFallbackView(
                lang = lang,
                onRequestPermission = {
                    permissionLauncher.launch(Manifest.permission.CAMERA)
                }
            )
        }

        // 2. AR Hologram Virtual Projection & Interactive Hotspots
        ArOverlayCanvas(
            artifact = currentArtifact,
            mode = visualizationMode,
            onHotspotClicked = { hotspot ->
                selectedHotspot = hotspot
            },
            modifier = Modifier.fillMaxSize()
        )

        // 3. Top HUD: Status Bar & Controls (Lens, Torch, Info)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.85f),
                            Color.Black.copy(alpha = 0.4f),
                            Color.Transparent
                        )
                    )
                )
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // AR Live Status Pill
                Surface(
                    color = GoldPrimary,
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF1A130B))
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = AppStrings.arStatusActive(lang),
                            color = Color(0xFF1A130B),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Camera Controls: Audio TTS Narration, Torch & Lens Switch
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    IconButton(
                        onClick = {
                            if (ttsNarrator.isSpeaking) {
                                ttsNarrator.stop()
                            } else {
                                ttsNarrator.speak(currentArtifact.audioStory(lang), lang)
                            }
                        },
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(if (ttsNarrator.isSpeaking) GoldPrimary else Color.Black.copy(alpha = 0.5f))
                            .testTag("ar_narrate_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.VolumeUp,
                            contentDescription = "Audio Guide (TTS)",
                            tint = if (ttsNarrator.isSpeaking) Color(0xFF1A130B) else Color.White
                        )
                    }

                    if (hasCameraPermission) {
                        IconButton(
                            onClick = {
                                isTorchOn = !isTorchOn
                                activeCamera?.cameraControl?.enableTorch(isTorchOn)
                            },
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(Color.Black.copy(alpha = 0.5f))
                                .testTag("ar_toggle_torch")
                        ) {
                            Icon(
                                imageVector = if (isTorchOn) Icons.Default.FlashOn else Icons.Default.FlashOff,
                                contentDescription = "Flash",
                                tint = if (isTorchOn) GoldPrimary else Color.White
                            )
                        }

                        IconButton(
                            onClick = {
                                lensFacing = if (lensFacing == CameraSelector.LENS_FACING_BACK) {
                                    CameraSelector.LENS_FACING_FRONT
                                } else {
                                    CameraSelector.LENS_FACING_BACK
                                }
                            },
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(Color.Black.copy(alpha = 0.5f))
                                .testTag("ar_flip_camera")
                        ) {
                            Icon(
                                imageVector = Icons.Default.FlipCameraAndroid,
                                contentDescription = "Flip",
                                tint = Color.White
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Artifact Title & Date
            Column {
                Text(
                    text = currentArtifact.title(lang),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = currentArtifact.originalYear(lang),
                    style = MaterialTheme.typography.labelSmall,
                    color = GoldPrimary
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Artifact Selector Chips
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                allArtifacts.forEach { artifact ->
                    val isSelected = artifact.id == currentArtifact.id
                    Surface(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { selectedArtifactId = artifact.id }
                            .testTag("ar_artifact_chip_${artifact.id}"),
                        color = if (isSelected) GoldPrimary else Color.Black.copy(alpha = 0.6f),
                        border = if (!isSelected) CardDefaults.outlinedCardBorder() else null,
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = artifact.title(lang).take(20) + "...",
                            color = if (isSelected) Color(0xFF1A130B) else Color.White,
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        )
                    }
                }
            }
        }

        // 4. Bottom Controls: Visualization Mode Tabs & Snapshot Button
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.6f),
                            Color.Black.copy(alpha = 0.95f)
                        )
                    )
                )
                .padding(horizontal = 16.dp, vertical = 14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Hotspot Hint Banner
            Surface(
                color = Color.Black.copy(alpha = 0.65f),
                shape = RoundedCornerShape(20.dp),
                border = CardDefaults.outlinedCardBorder()
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = GoldPrimary,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = AppStrings.arHotspotHint(lang),
                        color = Color.White,
                        fontSize = 11.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Visualization Mode Switcher
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Black.copy(alpha = 0.7f), RoundedCornerShape(14.dp))
                    .padding(4.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ArModeButton(
                    title = AppStrings.arModeRestored(lang),
                    isSelected = visualizationMode == ArVisualizationMode.RESTORED,
                    onClick = { visualizationMode = ArVisualizationMode.RESTORED },
                    modifier = Modifier.weight(1f)
                )
                ArModeButton(
                    title = AppStrings.arModeOriginalColors(lang),
                    isSelected = visualizationMode == ArVisualizationMode.ORIGINAL_COLORS,
                    onClick = { visualizationMode = ArVisualizationMode.ORIGINAL_COLORS },
                    modifier = Modifier.weight(1f)
                )
                ArModeButton(
                    title = AppStrings.arModeBlueprint(lang),
                    isSelected = visualizationMode == ArVisualizationMode.BLUEPRINT,
                    onClick = { visualizationMode = ArVisualizationMode.BLUEPRINT },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Shutter Snapshot Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(68.dp)
                        .clip(CircleShape)
                        .border(3.dp, GoldPrimary, CircleShape)
                        .background(Color.White.copy(alpha = 0.2f))
                        .clickable {
                            showSnapshotFlash = true
                            showSnapshotDialog = true
                        }
                        .testTag("ar_capture_snapshot_btn"),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape)
                            .background(GoldPrimary),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CameraAlt,
                            contentDescription = "Capture",
                            tint = Color(0xFF1A130B),
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
        }

        // Shutter Camera Flash Animation Effect
        LaunchedEffect(showSnapshotFlash) {
            if (showSnapshotFlash) {
                delay(120)
                showSnapshotFlash = false
            }
        }
        if (showSnapshotFlash) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
            )
        }

        // Hotspot Information Dialog
        selectedHotspot?.let { hotspot ->
            AlertDialog(
                onDismissRequest = { selectedHotspot = null },
                icon = {
                    Icon(
                        imageVector = Icons.Default.ViewInAr,
                        contentDescription = null,
                        tint = GoldPrimary,
                        modifier = Modifier.size(32.dp)
                    )
                },
                title = {
                    Text(
                        text = hotspot.title(lang),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                },
                text = {
                    Text(
                        text = hotspot.description(lang),
                        style = MaterialTheme.typography.bodyMedium,
                        lineHeight = 22.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                },
                confirmButton = {
                    Button(
                        onClick = { selectedHotspot = null },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = GoldPrimary,
                            contentColor = Color(0xFF1A130B)
                        ),
                        modifier = Modifier.testTag("close_hotspot_dialog_btn")
                    ) {
                        Text(AppStrings.understandSecretButton(lang), fontWeight = FontWeight.Bold)
                    }
                }
            )
        }

        // Souvenir AR Snapshot Captured Dialog
        if (showSnapshotDialog) {
            AlertDialog(
                onDismissRequest = { showSnapshotDialog = false },
                icon = {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(GoldPrimary),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CameraAlt,
                            contentDescription = null,
                            tint = Color(0xFF1A130B),
                            modifier = Modifier.size(26.dp)
                        )
                    }
                },
                title = {
                    Text(
                        text = AppStrings.snapshotSavedTitle(lang),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                },
                text = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = if (lang == AppLanguage.ARABIC) {
                                "تم التقاط صورة تذكارية مدمجة لـ ${currentArtifact.title(lang)} بالواقع المعزز (AR) مع المظهر المرمم وختم التوثيق الأثري الرسمي."
                            } else {
                                "Souvenir photo of ${currentArtifact.title(lang)} captured via Augmented Reality (AR) with restored ancient view and official archaeological certification seal."
                            },
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            lineHeight = 22.sp
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Surface(
                            color = GoldPrimary.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = AppStrings.snapshotStamp(lang),
                                color = GoldDark,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = { showSnapshotDialog = false },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = GoldPrimary,
                            contentColor = Color(0xFF1A130B)
                        ),
                        modifier = Modifier.testTag("dismiss_snapshot_dialog")
                    ) {
                        Text(AppStrings.continueExploring(lang), fontWeight = FontWeight.Bold)
                    }
                }
            )
        }
    }
}

@Composable
private fun ArModeButton(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(if (isSelected) GoldPrimary else Color.Transparent)
            .clickable { onClick() }
            .padding(vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            color = if (isSelected) Color(0xFF1A130B) else Color.White.copy(alpha = 0.8f),
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Composable
private fun CameraFallbackView(
    lang: AppLanguage,
    onRequestPermission: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF1A1612), Color(0xFF0D0B09))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(GoldPrimary.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.CameraAlt,
                    contentDescription = null,
                    tint = GoldPrimary,
                    modifier = Modifier.size(34.dp)
                )
            }
            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text = if (lang == AppLanguage.ARABIC) "كاميرا الواقع المعزز (AR)" else "Augmented Reality (AR) Camera",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = if (lang == AppLanguage.ARABIC) {
                    "يتم الآن عرض المجسمات والمعالم الأثرية الافتراضية فوق بيئة المحاكاة. لتشغيل الكاميرا الحقيقية يرجى منح إذن الكاميرا."
                } else {
                    "Virtual archaeological monuments are now rendered in simulation mode. Grant camera permission to project over real camera feed."
                },
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
                color = Color.White.copy(alpha = 0.7f),
                lineHeight = 20.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onRequestPermission,
                colors = ButtonDefaults.buttonColors(
                    containerColor = GoldPrimary,
                    contentColor = Color(0xFF1A130B)
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.testTag("request_camera_perm_btn")
            ) {
                Text(
                    text = if (lang == AppLanguage.ARABIC) "منح إذن الكاميرا" else "Grant Camera Permission",
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
