package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.ViewInAr
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.ViewInAr
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.example.data.AppLanguage
import com.example.data.AppStrings
import com.example.data.EgyptTourRepository
import com.example.data.Monument
import com.example.ui.screens.ArCameraScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.MonumentDetailScreen
import com.example.ui.screens.RoutesMapScreen
import com.example.ui.screens.VisitorGuideScreen
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.MyApplicationTheme

enum class MainTab(
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val testTag: String
) {
    EXPLORE(Icons.Filled.Explore, Icons.Outlined.Explore, "nav_tab_explore"),
    ROUTES(Icons.Filled.Map, Icons.Outlined.Map, "nav_tab_routes"),
    AR_CAMERA(Icons.Filled.ViewInAr, Icons.Outlined.ViewInAr, "nav_tab_ar"),
    GUIDE(Icons.Filled.Info, Icons.Outlined.Info, "nav_tab_guide");

    fun title(lang: AppLanguage): String = when (this) {
        EXPLORE -> AppStrings.tabExplore(lang)
        ROUTES -> AppStrings.tabRoutes(lang)
        AR_CAMERA -> AppStrings.tabAr(lang)
        GUIDE -> AppStrings.tabGuide(lang)
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                OmElDoniaApp()
            }
        }
    }
}

@Composable
fun OmElDoniaApp() {
    var currentLanguage by remember { mutableStateOf(AppLanguage.ARABIC) }
    var currentTab by remember { mutableStateOf(MainTab.EXPLORE) }
    var selectedMonument by remember { mutableStateOf<Monument?>(null) }
    var targetArArtifactId by remember { mutableStateOf<String?>(null) }
    var targetRouteId by remember { mutableStateOf<String?>(null) }

    val bookmarkedMonuments = remember {
        mutableStateListOf<Monument>().apply {
            add(EgyptTourRepository.monuments[0])
        }
    }

    val layoutDirection = if (currentLanguage.isRtl) {
        LayoutDirection.Rtl
    } else {
        LayoutDirection.Ltr
    }

    CompositionLocalProvider(LocalLayoutDirection provides layoutDirection) {
        Scaffold(
            contentWindowInsets = WindowInsets.safeDrawing,
            bottomBar = {
                if (selectedMonument == null) {
                    NavigationBar(
                        containerColor = MaterialTheme.colorScheme.surface,
                        tonalElevation = 8.dp,
                        modifier = Modifier
                            .windowInsetsPadding(WindowInsets.navigationBars)
                            .testTag("main_bottom_nav_bar")
                    ) {
                        MainTab.values().forEach { tab ->
                            val selected = currentTab == tab
                            NavigationBarItem(
                                selected = selected,
                                onClick = {
                                    currentTab = tab
                                    selectedMonument = null
                                },
                                icon = {
                                    Icon(
                                        imageVector = if (selected) tab.selectedIcon else tab.unselectedIcon,
                                        contentDescription = tab.title(currentLanguage)
                                    )
                                },
                                label = {
                                    Text(
                                        text = tab.title(currentLanguage),
                                        fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
                                    )
                                },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                                    selectedTextColor = GoldPrimary,
                                    indicatorColor = GoldPrimary,
                                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                                ),
                                modifier = Modifier.testTag(tab.testTag)
                            )
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxSize()
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                if (selectedMonument != null) {
                    val monument = selectedMonument!!
                    MonumentDetailScreen(
                        monument = monument,
                        lang = currentLanguage,
                        onBack = { selectedMonument = null },
                        onOpenAr = { artifactId ->
                            targetArArtifactId = artifactId
                            selectedMonument = null
                            currentTab = MainTab.AR_CAMERA
                        },
                        isBookmarked = bookmarkedMonuments.any { it.id == monument.id },
                        onToggleBookmark = { m ->
                            if (bookmarkedMonuments.any { it.id == m.id }) {
                                bookmarkedMonuments.removeAll { it.id == m.id }
                            } else {
                                bookmarkedMonuments.add(m)
                            }
                        }
                    )
                } else {
                    when (currentTab) {
                        MainTab.EXPLORE -> {
                            HomeScreen(
                                lang = currentLanguage,
                                onToggleLanguage = {
                                    currentLanguage = if (currentLanguage == AppLanguage.ARABIC) {
                                        AppLanguage.ENGLISH
                                    } else {
                                        AppLanguage.ARABIC
                                    }
                                },
                                onSelectLanguage = { newLang ->
                                    currentLanguage = newLang
                                },
                                onMonumentClick = { monument ->
                                    selectedMonument = monument
                                },
                                onNavigateToAr = { artifactId ->
                                    targetArArtifactId = artifactId
                                    currentTab = MainTab.AR_CAMERA
                                },
                                onNavigateToRoutes = { routeId ->
                                    targetRouteId = routeId
                                    currentTab = MainTab.ROUTES
                                }
                            )
                        }

                        MainTab.ROUTES -> {
                            RoutesMapScreen(
                                lang = currentLanguage,
                                initialRouteId = targetRouteId,
                                onMonumentClick = { monument ->
                                    selectedMonument = monument
                                }
                            )
                        }

                        MainTab.AR_CAMERA -> {
                            ArCameraScreen(
                                lang = currentLanguage,
                                initialArtifactId = targetArArtifactId
                            )
                        }

                        MainTab.GUIDE -> {
                            VisitorGuideScreen(
                                lang = currentLanguage,
                                onLanguageChange = { newLang ->
                                    currentLanguage = newLang
                                },
                                bookmarkedMonuments = bookmarkedMonuments,
                                onMonumentClick = { monument ->
                                    selectedMonument = monument
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
