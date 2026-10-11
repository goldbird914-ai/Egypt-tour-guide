package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import com.example.data.ArArtifact
import com.example.data.ArHotspot
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.NileTurquoise
import kotlin.math.cos
import kotlin.math.sin

enum class ArVisualizationMode {
    RESTORED,        // المظهر المرمم الخالد
    ORIGINAL_COLORS, // ألوان الفراعنة الأصلية
    BLUEPRINT        // المخطط الهندسي القديم
}

@Composable
fun ArOverlayCanvas(
    artifact: ArArtifact,
    mode: ArVisualizationMode,
    onHotspotClicked: (ArHotspot) -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "ar_infinite")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.35f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )

    val rotationAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(20000),
            repeatMode = RepeatMode.Restart
        ),
        label = "ar_rotation"
    )

    Box(modifier = modifier.fillMaxSize()) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(artifact.id) {
                    detectTapGestures { tapOffset ->
                        val w = size.width
                        val h = size.height

                        artifact.hotspots.forEach { hotspot ->
                            val hx = hotspot.xFraction * w
                            val hy = hotspot.yFraction * h
                            val distSq = (tapOffset.x - hx) * (tapOffset.x - hx) + (tapOffset.y - hy) * (tapOffset.y - hy)
                            if (distSq < 50f * 50f) {
                                onHotspotClicked(hotspot)
                            }
                        }
                    }
                }
        ) {
            val w = size.width
            val h = size.height

            // 1. Draw AR Futuristic HUD Framework
            drawArHud(w, h, rotationAngle, mode)

            // 2. Render 3D Artifact Virtual Model Hologram
            when (artifact.id) {
                "ar_sphinx" -> drawSphinxArModel(w, h, mode)
                "ar_pyramid" -> drawPyramidArModel(w, h, mode)
                "ar_tut_mask" -> drawTutMaskArModel(w, h, mode)
                else -> drawKarnakColumnsArModel(w, h, mode)
            }

            // 3. Draw AR Interactive Hotspots
            artifact.hotspots.forEach { hotspot ->
                val hx = hotspot.xFraction * w
                val hy = hotspot.yFraction * h

                // Outer pulsing beacon ring
                drawCircle(
                    color = GoldPrimary.copy(alpha = 0.35f),
                    radius = 20f * pulseScale,
                    center = Offset(hx, hy),
                    style = Stroke(width = 2f)
                )

                // Mid glowing circle
                drawCircle(
                    color = if (mode == ArVisualizationMode.BLUEPRINT) NileTurquoise else GoldPrimary,
                    radius = 12f,
                    center = Offset(hx, hy)
                )

                // Center white target pip
                drawCircle(
                    color = Color.White,
                    radius = 5f,
                    center = Offset(hx, hy)
                )

                // Crosshair whiskers on hotspot
                val whiskerLen = 18f
                val whiskerColor = if (mode == ArVisualizationMode.BLUEPRINT) NileTurquoise else GoldPrimary
                drawLine(whiskerColor, Offset(hx - whiskerLen, hy), Offset(hx - 8f, hy), strokeWidth = 2f)
                drawLine(whiskerColor, Offset(hx + 8f, hy), Offset(hx + whiskerLen, hy), strokeWidth = 2f)
                drawLine(whiskerColor, Offset(hx, hy - whiskerLen), Offset(hx, hy - 8f), strokeWidth = 2f)
                drawLine(whiskerColor, Offset(hx, hy + 8f), Offset(hx, hy + whiskerLen), strokeWidth = 2f)
            }
        }
    }
}

private fun DrawScope.drawArHud(w: Float, h: Float, rotation: Float, mode: ArVisualizationMode) {
    val hudColor = when (mode) {
        ArVisualizationMode.BLUEPRINT -> NileTurquoise.copy(alpha = 0.5f)
        ArVisualizationMode.ORIGINAL_COLORS -> Color(0xFFFFCC00).copy(alpha = 0.5f)
        ArVisualizationMode.RESTORED -> GoldPrimary.copy(alpha = 0.5f)
    }

    // Four Corner Viewfinder Brackets
    val bracketLen = 36f
    val pad = 24f
    val strokeWidth = 3f

    // Top-left
    drawLine(hudColor, Offset(pad, pad), Offset(pad + bracketLen, pad), strokeWidth = strokeWidth)
    drawLine(hudColor, Offset(pad, pad), Offset(pad, pad + bracketLen), strokeWidth = strokeWidth)

    // Top-right
    drawLine(hudColor, Offset(w - pad, pad), Offset(w - pad - bracketLen, pad), strokeWidth = strokeWidth)
    drawLine(hudColor, Offset(w - pad, pad), Offset(w - pad, pad + bracketLen), strokeWidth = strokeWidth)

    // Bottom-left
    drawLine(hudColor, Offset(pad, h - pad), Offset(pad + bracketLen, h - pad), strokeWidth = strokeWidth)
    drawLine(hudColor, Offset(pad, h - pad), Offset(pad, h - pad - bracketLen), strokeWidth = strokeWidth)

    // Bottom-right
    drawLine(hudColor, Offset(w - pad, h - pad), Offset(w - pad - bracketLen, h - pad), strokeWidth = strokeWidth)
    drawLine(hudColor, Offset(w - pad, h - pad), Offset(w - pad, h - pad - bracketLen), strokeWidth = strokeWidth)

    // Horizon line with center pitch marker
    val horizonY = h * 0.5f
    val dashEffect = PathEffect.dashPathEffect(floatArrayOf(15f, 15f), 0f)
    drawLine(
        color = hudColor.copy(alpha = 0.25f),
        start = Offset(pad + 10f, horizonY),
        end = Offset(w - pad - 10f, horizonY),
        strokeWidth = 1.5f,
        pathEffect = dashEffect
    )

    // Center targeting reticle ring
    val cx = w * 0.5f
    val cy = h * 0.5f
    drawCircle(
        color = hudColor.copy(alpha = 0.2f),
        radius = 70f,
        center = Offset(cx, cy),
        style = Stroke(width = 1.5f, pathEffect = dashEffect)
    )
}

// AR Reconstruction: Great Sphinx of Giza with Restored Head, Royal Beard & Pharaonic Pigments
private fun DrawScope.drawSphinxArModel(w: Float, h: Float, mode: ArVisualizationMode) {
    val cx = w * 0.5f
    val cy = h * 0.48f

    val baseColor = when (mode) {
        ArVisualizationMode.RESTORED -> GoldPrimary
        ArVisualizationMode.ORIGINAL_COLORS -> Color(0xFFD35400) // Ancient Red Ochre
        ArVisualizationMode.BLUEPRINT -> NileTurquoise
    }

    val accentColor = when (mode) {
        ArVisualizationMode.RESTORED -> Color(0xFFF3E5AB)
        ArVisualizationMode.ORIGINAL_COLORS -> Color(0xFF1E88E5) // Lapis Lazuli royal blue
        ArVisualizationMode.BLUEPRINT -> Color(0xFF80D8CE)
    }

    // Lion Body Baseline
    val bodyPath = Path().apply {
        moveTo(cx - 160f, cy + 120f)
        lineTo(cx + 160f, cy + 120f) // paws base
        lineTo(cx + 175f, cy + 90f)
        quadraticTo(cx + 120f, cy + 40f, cx + 50f, cy + 30f) // back curve
        lineTo(cx - 50f, cy + 30f)
        quadraticTo(cx - 120f, cy + 40f, cx - 175f, cy + 90f)
        close()
    }

    drawPath(
        path = bodyPath,
        color = baseColor.copy(alpha = if (mode == ArVisualizationMode.BLUEPRINT) 0.15f else 0.45f)
    )
    drawPath(
        path = bodyPath,
        color = baseColor,
        style = Stroke(width = if (mode == ArVisualizationMode.BLUEPRINT) 2f else 3.5f)
    )

    // Royal Nemes Headdress & Face
    val headPath = Path().apply {
        moveTo(cx - 55f, cy - 80f)
        lineTo(cx + 55f, cy - 80f)
        lineTo(cx + 70f, cy - 20f) // Nemes wing right
        lineTo(cx + 45f, cy + 25f)
        lineTo(cx - 45f, cy + 25f)
        lineTo(cx - 70f, cy - 20f) // Nemes wing left
        close()
    }

    drawPath(
        path = headPath,
        color = accentColor.copy(alpha = 0.5f)
    )
    drawPath(
        path = headPath,
        color = accentColor,
        style = Stroke(width = 3.5f)
    )

    // Nemes Royal Stripes
    for (i in -3..3) {
        val yOffset = cy - 60f + i * 14f
        drawLine(
            color = if (mode == ArVisualizationMode.ORIGINAL_COLORS) GoldPrimary else baseColor,
            start = Offset(cx - 45f, yOffset),
            end = Offset(cx + 45f, yOffset),
            strokeWidth = 2f
        )
    }

    // Restored Pharaonic Braided Beard
    val beardPath = Path().apply {
        moveTo(cx - 14f, cy + 25f)
        lineTo(cx + 14f, cy + 25f)
        lineTo(cx + 10f, cy + 70f)
        lineTo(cx - 10f, cy + 70f)
        close()
    }
    drawPath(
        path = beardPath,
        color = GoldPrimary.copy(alpha = 0.8f)
    )
    drawPath(
        path = beardPath,
        color = GoldPrimary,
        style = Stroke(width = 2.5f)
    )

    // Restored Uraeus Cobra on forehead
    drawCircle(
        color = GoldPrimary,
        radius = 7f,
        center = Offset(cx, cy - 85f)
    )
}

// AR Reconstruction: Great Pyramid with Electrum Golden Capstone & Gleaming Tura Limestone Casing
private fun DrawScope.drawPyramidArModel(w: Float, h: Float, mode: ArVisualizationMode) {
    val cx = w * 0.5f
    val cy = h * 0.52f

    val baseWidth = 320f
    val pyrHeight = 240f

    val apex = Offset(cx, cy - pyrHeight * 0.5f)
    val leftBase = Offset(cx - baseWidth * 0.5f, cy + pyrHeight * 0.5f)
    val rightBase = Offset(cx + baseWidth * 0.5f, cy + pyrHeight * 0.5f)
    val centerRidge = Offset(cx + 25f, cy + pyrHeight * 0.5f)

    // Western slope (illuminated)
    val westFace = Path().apply {
        moveTo(apex.x, apex.y)
        lineTo(leftBase.x, leftBase.y)
        lineTo(centerRidge.x, centerRidge.y)
        close()
    }

    // Eastern slope (subtle shadow)
    val eastFace = Path().apply {
        moveTo(apex.x, apex.y)
        lineTo(centerRidge.x, centerRidge.y)
        lineTo(rightBase.x, rightBase.y)
        close()
    }

    val faceColor1 = when (mode) {
        ArVisualizationMode.RESTORED -> Color(0xFFFBF8F0) // Gleaming white Tura casing
        ArVisualizationMode.ORIGINAL_COLORS -> Color(0xFFFFEEBA) // Desert sunset reflection
        ArVisualizationMode.BLUEPRINT -> NileTurquoise.copy(alpha = 0.15f)
    }

    val faceColor2 = when (mode) {
        ArVisualizationMode.RESTORED -> Color(0xFFDFD7C7)
        ArVisualizationMode.ORIGINAL_COLORS -> Color(0xFFE2C48E)
        ArVisualizationMode.BLUEPRINT -> NileTurquoise.copy(alpha = 0.3f)
    }

    drawPath(path = westFace, color = faceColor1.copy(alpha = 0.55f))
    drawPath(path = eastFace, color = faceColor2.copy(alpha = 0.65f))

    val strokeCol = if (mode == ArVisualizationMode.BLUEPRINT) NileTurquoise else GoldPrimary
    drawPath(path = westFace, color = strokeCol, style = Stroke(width = 3f))
    drawPath(path = eastFace, color = strokeCol, style = Stroke(width = 3f))

    // Golden Electrum Capstone (Pyramidion)
    val capHeight = 35f
    val capWidth = 46f
    val capLeft = Offset(cx - capWidth * 0.5f, apex.y + capHeight)
    val capRight = Offset(cx + capWidth * 0.5f, apex.y + capHeight)

    val capstone = Path().apply {
        moveTo(apex.x, apex.y)
        lineTo(capLeft.x, capLeft.y)
        lineTo(capRight.x, capRight.y)
        close()
    }

    drawPath(path = capstone, color = GoldPrimary)
    drawPath(path = capstone, color = Color.White.copy(alpha = 0.8f), style = Stroke(width = 2.5f))

    // Sun Rays beaming from the Golden Capstone
    if (mode != ArVisualizationMode.BLUEPRINT) {
        for (i in -4..4) {
            val angle = -Math.PI / 2 + (i * 0.22)
            val rayLen = 90f
            val endX = apex.x + (cos(angle) * rayLen).toFloat()
            val endY = apex.y + (sin(angle) * rayLen).toFloat()
            drawLine(
                color = GoldPrimary.copy(alpha = 0.6f - Math.abs(i) * 0.1f),
                start = apex,
                end = Offset(endX, endY),
                strokeWidth = 2.5f
            )
        }
    }
}

// AR Reconstruction: Golden Mask of Tutankhamun with Royal Lapis Lazuli & Uraeus
private fun DrawScope.drawTutMaskArModel(w: Float, h: Float, mode: ArVisualizationMode) {
    val cx = w * 0.5f
    val cy = h * 0.46f

    val gold = GoldPrimary
    val lapis = Color(0xFF1565C0)

    // Mask outer contour
    val maskContour = Path().apply {
        moveTo(cx - 75f, cy - 90f)
        quadraticTo(cx, cy - 110f, cx + 75f, cy - 90f) // rounded top
        lineTo(cx + 85f, cy + 20f) // flared headdress side
        quadraticTo(cx + 40f, cy + 85f, cx, cy + 110f) // chin base
        quadraticTo(cx - 40f, cy + 85f, cx - 85f, cy + 20f)
        close()
    }

    drawPath(path = maskContour, color = gold.copy(alpha = 0.45f))
    drawPath(path = maskContour, color = gold, style = Stroke(width = 3.5f))

    // Alternating Nemes Stripes (Gold & Lapis Lazuli)
    for (i in 0..7) {
        val y = cy - 80f + i * 16f
        val col = if (i % 2 == 0) lapis else gold
        drawLine(
            color = col,
            start = Offset(cx - 65f + (i * 3f), y),
            end = Offset(cx + 65f - (i * 3f), y),
            strokeWidth = 5f
        )
    }

    // Eye outlines with prominent Egyptian kohl wings
    val eyeColor = if (mode == ArVisualizationMode.BLUEPRINT) NileTurquoise else Color.White
    drawCircle(color = eyeColor, radius = 9f, center = Offset(cx - 28f, cy - 10f))
    drawCircle(color = eyeColor, radius = 9f, center = Offset(cx + 28f, cy - 10f))
    drawCircle(color = Color.Black, radius = 4f, center = Offset(cx - 28f, cy - 10f))
    drawCircle(color = Color.Black, radius = 4f, center = Offset(cx + 28f, cy - 10f))

    // Royal Beard
    val beard = Path().apply {
        moveTo(cx - 10f, cy + 70f)
        lineTo(cx + 10f, cy + 70f)
        lineTo(cx + 7f, cy + 115f)
        lineTo(cx - 7f, cy + 115f)
        close()
    }
    drawPath(path = beard, color = lapis)
    drawPath(path = beard, color = gold, style = Stroke(width = 2f))
}

// AR Reconstruction: Karnak Hypostyle Columns with Papyrus Capitals
private fun DrawScope.drawKarnakColumnsArModel(w: Float, h: Float, mode: ArVisualizationMode) {
    val cx = w * 0.5f
    val cy = h * 0.48f

    val colColor = when (mode) {
        ArVisualizationMode.RESTORED -> Color(0xFFC7B198)
        ArVisualizationMode.ORIGINAL_COLORS -> Color(0xFF00897B) // Papyrus Green & Celestial Blue
        ArVisualizationMode.BLUEPRINT -> NileTurquoise
    }

    // Two monumental columns left & right
    val colXOffsets = listOf(-95f, 95f)

    colXOffsets.forEach { ox ->
        val x = cx + ox

        // Capital (Open Papyrus Flower)
        val capPath = Path().apply {
            moveTo(x - 45f, cy - 90f)
            quadraticTo(x, cy - 120f, x + 45f, cy - 90f)
            lineTo(x + 25f, cy - 50f)
            lineTo(x - 25f, cy - 50f)
            close()
        }
        drawPath(path = capPath, color = colColor.copy(alpha = 0.5f))
        drawPath(path = capPath, color = colColor, style = Stroke(width = 3f))

        // Column Shaft
        drawRect(
            color = colColor.copy(alpha = 0.35f),
            topLeft = Offset(x - 22f, cy - 50f),
            size = Size(44f, 170f)
        )
        drawRect(
            color = colColor,
            topLeft = Offset(x - 22f, cy - 50f),
            size = Size(44f, 170f),
            style = Stroke(width = 2.5f)
        )

        // Hieroglyphic bands across column shaft
        for (b in 0..4) {
            val by = cy - 20f + b * 28f
            drawLine(
                color = GoldPrimary,
                start = Offset(x - 20f, by),
                end = Offset(x + 20f, by),
                strokeWidth = 2f
            )
        }
    }

    // Architrave (Roof Beam connecting columns)
    drawRect(
        color = colColor.copy(alpha = 0.6f),
        topLeft = Offset(cx - 140f, cy - 130f),
        size = Size(280f, 25f)
    )
    drawRect(
        color = GoldPrimary,
        topLeft = Offset(cx - 140f, cy - 130f),
        size = Size(280f, 25f),
        style = Stroke(width = 3f)
    )
}
