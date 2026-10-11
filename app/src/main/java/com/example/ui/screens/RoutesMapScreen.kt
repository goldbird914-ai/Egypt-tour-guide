package com.example.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.EmojiFlags
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Route
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AppLanguage
import com.example.data.AppStrings
import com.example.data.EgyptTourRepository
import com.example.data.Monument
import com.example.data.RouteStop
import com.example.data.TourRoute
import com.example.ui.components.RouteMapCanvas
import com.example.ui.theme.GoldDark
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.NileTurquoise

@Composable
fun RoutesMapScreen(
    lang: AppLanguage,
    initialRouteId: String? = null,
    onMonumentClick: (Monument) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val allRoutes = EgyptTourRepository.tourRoutes

    var selectedRouteId by remember(initialRouteId) {
        mutableStateOf(initialRouteId ?: allRoutes.first().id)
    }

    val currentRoute = allRoutes.find { it.id == selectedRouteId } ?: allRoutes.first()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("routes_map_screen_list")
    ) {
        // Header Bar
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(GoldPrimary.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Route,
                            contentDescription = null,
                            tint = GoldPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = AppStrings.routesScreenTitle(lang),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = AppStrings.routesScreenSubtitle(lang),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        // Horizontal Route Selector Chips
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                allRoutes.forEach { route ->
                    FilterChip(
                        selected = route.id == currentRoute.id,
                        onClick = { selectedRouteId = route.id },
                        label = { Text(route.region(lang)) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = GoldPrimary,
                            selectedLabelColor = Color(0xFF1A130B)
                        ),
                        modifier = Modifier.testTag("select_route_chip_${route.id}")
                    )
                }
            }
        }

        // Active Route Summary Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .testTag("current_route_summary_card"),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                border = CardDefaults.outlinedCardBorder()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Surface(
                        color = GoldPrimary,
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = currentRoute.region(lang),
                            color = Color(0xFF1A130B),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = currentRoute.title(lang),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = currentRoute.subtitle(lang),
                        style = MaterialTheme.typography.bodySmall,
                        color = GoldDark
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = currentRoute.description(lang),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 22.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Metrics Row: Duration, Distance, Difficulty, Transport
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        RouteMetricItem(
                            icon = Icons.Default.AccessTime,
                            title = AppStrings.estimatedDuration(lang),
                            value = "${currentRoute.totalDurationHours} ${AppStrings.hoursUnit(lang)}"
                        )
                        RouteMetricItem(
                            icon = Icons.Default.Map,
                            title = AppStrings.totalDistance(lang),
                            value = "${currentRoute.totalDistanceKm} ${AppStrings.kmUnit(lang)}"
                        )
                        RouteMetricItem(
                            icon = Icons.Default.EmojiFlags,
                            title = AppStrings.trailDifficulty(lang),
                            value = currentRoute.difficulty(lang)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Transport info & Budget
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "${AppStrings.transportPrefix(lang)}${currentRoute.transportType(lang)}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "${AppStrings.budgetPrefix(lang)}${currentRoute.estimatedBudgetEgp} ${AppStrings.egpUnit(lang)}",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = NileTurquoise
                        )
                    }
                }
            }
        }

        // Interactive Map Canvas Component
        item {
            Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                RouteMapCanvas(
                    route = currentRoute,
                    lang = lang,
                    onStopSelected = { stop ->
                        val monument = EgyptTourRepository.getMonumentById(stop.monumentId)
                        if (monument != null) {
                            onMonumentClick(monument)
                        }
                    }
                )
            }
        }

        // Timeline Header
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 20.dp, bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = AppStrings.routeStopsHeader(lang),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = AppStrings.stopsCount(currentRoute.stops.size, lang),
                    style = MaterialTheme.typography.labelSmall,
                    color = GoldDark
                )
            }
        }

        // Step-by-Step Stops Timeline
        itemsIndexed(currentRoute.stops) { index, stop ->
            RouteStopTimelineItem(
                stop = stop,
                lang = lang,
                isLast = index == currentRoute.stops.size - 1,
                onClick = {
                    val m = EgyptTourRepository.getMonumentById(stop.monumentId)
                    if (m != null) onMonumentClick(m)
                },
                onNavigateGoogleMaps = {
                    openStopGoogleMaps(context, stop, lang)
                },
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }

        // Essential Itinerary Tips Card
        val tips = currentRoute.essentialTips(lang)
        if (tips.isNotEmpty()) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = GoldPrimary.copy(alpha = 0.1f)
                    ),
                    shape = RoundedCornerShape(14.dp),
                    border = CardDefaults.outlinedCardBorder()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = AppStrings.routeTipsHeader(lang),
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = GoldPrimary
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        tips.forEach { tip ->
                            Text(
                                text = "✓ $tip",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface,
                                lineHeight = 20.sp,
                                modifier = Modifier.padding(vertical = 3.dp)
                            )
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(90.dp))
        }
    }
}

@Composable
private fun RouteMetricItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    value: String
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = GoldPrimary,
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun RouteStopTimelineItem(
    stop: RouteStop,
    lang: AppLanguage,
    isLast: Boolean,
    onClick: () -> Unit,
    onNavigateGoogleMaps: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .testTag("route_stop_item_${stop.monumentId}"),
        verticalAlignment = Alignment.Top
    ) {
        // Timeline Indicator Column
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(36.dp)
        ) {
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
                    fontSize = 13.sp
                )
            }
            if (!isLast) {
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .height(80.dp)
                        .background(GoldPrimary.copy(alpha = 0.4f))
                )
            }
        }

        Spacer(modifier = Modifier.width(10.dp))

        // Stop Content Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp)
                .clickable { onClick() },
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stop.stopTitle(lang),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.weight(1f)
                    )

                    Button(
                        onClick = onNavigateGoogleMaps,
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = NileTurquoise,
                            contentColor = Color.White
                        ),
                        modifier = Modifier
                            .height(30.dp)
                            .testTag("stop_nav_btn_${stop.order}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Navigation,
                            contentDescription = null,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = AppStrings.navigateStop(lang), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = stop.stopHighlights(lang),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = if (lang == AppLanguage.ARABIC) "⏱️ قضاء: ${stop.stopDurationMinutes} دقيقة" else "⏱️ Stay: ${stop.stopDurationMinutes} min",
                        style = MaterialTheme.typography.labelSmall,
                        color = GoldDark
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { onClick() }
                    ) {
                        Text(
                            text = AppStrings.siteDetailsButton(lang),
                            style = MaterialTheme.typography.labelSmall,
                            color = NileTurquoise,
                            fontWeight = FontWeight.Bold
                        )
                        Icon(
                            imageVector = Icons.Default.ChevronLeft,
                            contentDescription = null,
                            tint = NileTurquoise,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}

private fun openStopGoogleMaps(context: Context, stop: RouteStop, lang: AppLanguage) {
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
