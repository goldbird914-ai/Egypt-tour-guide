package com.example.ui.components

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.material.icons.filled.Directions
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AppLanguage
import com.example.data.AppStrings
import com.example.data.RouteStop
import com.example.data.TourRoute
import com.example.ui.theme.GoldDark
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.NileTurquoise
import com.example.ui.theme.SandBackgroundDark
import kotlin.math.max

@Composable
fun RouteMapCanvas(
    route: TourRoute,
    lang: AppLanguage = AppLanguage.ARABIC,
    onStopSelected: (RouteStop) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var selectedStopIndex by remember(route.id) { mutableIntStateOf(0) }

    val stops = route.stops
    val currentSelectedStop = stops.getOrNull(selectedStopIndex) ?: stops.firstOrNull()

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("route_map_card"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        border = CardDefaults.outlinedCardBorder()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header with Map Badge & Google Maps direct action
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(NileTurquoise.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Map,
                            contentDescription = "Map",
                            tint = NileTurquoise,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = AppStrings.trailMapTitle(lang),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = AppStrings.keyStopsSubtitle(stops.size, lang),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Button(
                    onClick = {
                        openRouteInGoogleMaps(context, route)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GoldPrimary,
                        contentColor = Color(0xFF1A130B)
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.testTag("open_in_google_maps_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.Directions,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Google Maps",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Interactive Map View Canvas
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(SandBackgroundDark)
                    .testTag("interactive_map_canvas")
            ) {
                val lats = route.polylinePoints.map { it.first } + stops.map { it.latitude }
                val lngs = route.polylinePoints.map { it.second } + stops.map { it.longitude }
                val minLat = lats.minOrNull() ?: 29.0
                val maxLat = lats.maxOrNull() ?: 30.0
                val minLng = lngs.minOrNull() ?: 31.0
                val maxLng = lngs.maxOrNull() ?: 32.0

                val latSpan = max(maxLat - minLat, 0.005)
                val lngSpan = max(maxLng - minLng, 0.005)

                Canvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                        .pointerInput(route.id) {
                            detectTapGestures { tapOffset ->
                                val canvasWidth = size.width
                                val canvasHeight = size.height

                                stops.forEachIndexed { index, stop ->
                                    val nx = ((stop.longitude - minLng) / lngSpan).toFloat()
                                    val ny = 1f - ((stop.latitude - minLat) / latSpan).toFloat()

                                    val px = (0.12f + nx * 0.76f) * canvasWidth
                                    val py = (0.12f + ny * 0.76f) * canvasHeight

                                    val dist = (tapOffset.x - px) * (tapOffset.x - px) +
                                            (tapOffset.y - py) * (tapOffset.y - py)
                                    if (dist < 45f * 45f) {
                                        selectedStopIndex = index
                                        onStopSelected(stop)
                                    }
                                }
                            }
                        }
                ) {
                    val w = size.width
                    val h = size.height

                    val gridColor = Color(0xFF262019)
                    val step = 30.dp.toPx()
                    for (x in 0..(w / step).toInt()) {
                        drawLine(
                            color = gridColor,
                            start = Offset(x * step, 0f),
                            end = Offset(x * step, h),
                            strokeWidth = 1f
                        )
                    }
                    for (y in 0..(h / step).toInt()) {
                        drawLine(
                            color = gridColor,
                            start = Offset(0f, y * step),
                            end = Offset(w, y * step),
                            strokeWidth = 1f
                        )
                    }

                    // Route Polyline
                    if (route.polylinePoints.size >= 2) {
                        val path = Path()
                        route.polylinePoints.forEachIndexed { i, pt ->
                            val nx = ((pt.second - minLng) / lngSpan).toFloat()
                            val ny = 1f - ((pt.first - minLat) / latSpan).toFloat()
                            val px = (0.12f + nx * 0.76f) * w
                            val py = (0.12f + ny * 0.76f) * h

                            if (i == 0) {
                                path.moveTo(px, py)
                            } else {
                                path.lineTo(px, py)
                            }
                        }

                        drawPath(
                            path = path,
                            color = GoldPrimary.copy(alpha = 0.35f),
                            style = Stroke(width = 8.dp.toPx())
                        )
                        drawPath(
                            path = path,
                            color = GoldPrimary,
                            style = Stroke(
                                width = 3.dp.toPx(),
                                pathEffect = PathEffect.dashPathEffect(floatArrayOf(20f, 12f), 0f)
                            )
                        )
                    }

                    // Monument Waypoint Pins
                    stops.forEachIndexed { index, stop ->
                        val nx = ((stop.longitude - minLng) / lngSpan).toFloat()
                        val ny = 1f - ((stop.latitude - minLat) / latSpan).toFloat()
                        val px = (0.12f + nx * 0.76f) * w
                        val py = (0.12f + ny * 0.76f) * h

                        val isSelected = index == selectedStopIndex

                        if (isSelected) {
                            drawCircle(
                                color = GoldPrimary.copy(alpha = 0.25f),
                                radius = 22.dp.toPx(),
                                center = Offset(px, py)
                            )
                            drawCircle(
                                color = GoldPrimary.copy(alpha = 0.5f),
                                radius = 16.dp.toPx(),
                                center = Offset(px, py)
                            )
                        }

                        drawCircle(
                            color = if (isSelected) GoldPrimary else NileTurquoise,
                            radius = if (isSelected) 11.dp.toPx() else 8.dp.toPx(),
                            center = Offset(px, py)
                        )
                        drawCircle(
                            color = Color(0xFF12100E),
                            radius = if (isSelected) 4.5.dp.toPx() else 3.5.dp.toPx(),
                            center = Offset(px, py)
                        )
                    }
                }

                // Legend Overlay
                Surface(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(8.dp),
                    color = Color.Black.copy(alpha = 0.65f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(GoldPrimary)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = AppStrings.curatedPathLegend(lang),
                            color = Color.White,
                            fontSize = 10.sp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(NileTurquoise)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = AppStrings.monumentPointLegend(lang),
                            color = Color.White,
                            fontSize = 10.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Current Selected Stop Card
            currentSelectedStop?.let { stop ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("selected_stop_detail_card"),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    border = CardDefaults.outlinedCardBorder()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(28.dp)
                                        .clip(CircleShape)
                                        .background(GoldPrimary),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "${stop.order}",
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF1A130B),
                                        fontSize = 14.sp
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = stop.stopTitle(lang),
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }

                            IconButtonSmall(
                                icon = Icons.Default.Navigation,
                                text = AppStrings.navigateStop(lang),
                                onClick = {
                                    openStopInGoogleMaps(context, stop, lang)
                                }
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = stop.stopHighlights(lang),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = AppStrings.stayDuration(stop.stopDurationMinutes, lang),
                                style = MaterialTheme.typography.labelSmall,
                                color = GoldDark
                            )
                            if (stop.walkingTimeFromPreviousMin > 0) {
                                Text(
                                    text = AppStrings.walkingFromPrevious(stop.walkingTimeFromPreviousMin, lang),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = NileTurquoise
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun IconButtonSmall(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .testTag("navigate_to_stop_btn"),
        color = NileTurquoise.copy(alpha = 0.15f),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = NileTurquoise,
                modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = text,
                color = NileTurquoise,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

private fun openRouteInGoogleMaps(context: Context, route: TourRoute) {
    val stops = route.stops
    if (stops.isEmpty()) return

    val firstStop = stops.first()
    val lastStop = stops.last()

    val waypoints = if (stops.size > 2) {
        stops.subList(1, stops.size - 1)
            .joinToString("|") { "${it.latitude},${it.longitude}" }
    } else ""

    val uriString = buildString {
        append("https://www.google.com/maps/dir/?api=1")
        append("&origin=${firstStop.latitude},${firstStop.longitude}")
        append("&destination=${lastStop.latitude},${lastStop.longitude}")
        if (waypoints.isNotEmpty()) {
            append("&waypoints=${Uri.encode(waypoints)}")
        }
        append("&travelmode=walking")
    }

    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uriString)).apply {
        setPackage("com.google.android.apps.maps")
    }

    try {
        context.startActivity(intent)
    } catch (_: Exception) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(uriString))
        try {
            context.startActivity(browserIntent)
        } catch (_: Exception) {}
    }
}

private fun openStopInGoogleMaps(context: Context, stop: RouteStop, lang: AppLanguage) {
    val title = stop.stopTitle(lang)
    val uri = Uri.parse("geo:${stop.latitude},${stop.longitude}?q=${stop.latitude},${stop.longitude}(${Uri.encode(title)})")
    val intent = Intent(Intent.ACTION_VIEW, uri).apply {
        setPackage("com.google.android.apps.maps")
    }

    try {
        context.startActivity(intent)
    } catch (_: Exception) {
        val webUri = Uri.parse("https://www.google.com/maps/search/?api=1&query=${stop.latitude},${stop.longitude}")
        val webIntent = Intent(Intent.ACTION_VIEW, webUri)
        try {
            context.startActivity(webIntent)
        } catch (_: Exception) {}
    }
}
