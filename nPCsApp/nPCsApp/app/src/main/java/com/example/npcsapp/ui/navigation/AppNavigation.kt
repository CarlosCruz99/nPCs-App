package com.example.npcsapp.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.npcsapp.ui.screens.BuildDetailScreen
import com.example.npcsapp.ui.screens.BuildsScreen
import com.example.npcsapp.ui.screens.ComponentSelectScreen
import com.example.npcsapp.ui.screens.GPUDetailScreen
import com.example.npcsapp.ui.screens.HomeScreen
import com.example.npcsapp.ui.screens.MarketScreen
import com.example.npcsapp.ui.screens.SearchScreen
import com.example.npcsapp.ui.screens.SellScreen
import com.example.npcsapp.ui.screens.WelcomeScreen
import com.example.npcsapp.ui.theme.NeonBlue
import com.example.npcsapp.ui.theme.OnSurfaceVariant
import com.example.npcsapp.ui.theme.PrimaryContainer
import com.example.npcsapp.ui.theme.SurfaceContainerHigh
import com.example.npcsapp.viewmodel.BuildViewModel
import com.example.npcsapp.viewmodel.GPUViewModel

// ── Sealed class: all 5 tabs ──────────────────────────────────────────────

sealed class Screen(
    val route: String,
    val label: String,
    val icon: ImageVector,
    /** false = tab button shown but does nothing yet */
    val enabled: Boolean = true
) {
    object Home : Screen(
        route = "home_screen",
        label = "Home",
        icon  = Icons.Default.Home
    )
    object Search : Screen(
        route   = "search_screen",
        label   = "Search",
        icon    = Icons.Default.Search,
        enabled = true
    )
    object Builds : Screen(
        route = "builds_screen",
        label = "Build",
        icon  = Icons.Default.Build
    )
    object Market : Screen(
        route   = "market_screen",
        label   = "Market",
        icon    = Icons.Default.ShoppingCart,
        enabled = true
    )
    object Profile : Screen(
        route   = "profile_screen",
        label   = "Profile",
        icon    = Icons.Default.Person,
        enabled = false   // ← pantalla pendiente de implementar
    )
}

// ── Top-level tab list shown in the nav bar ───────────────────────────────
private val navItems = listOf(
    Screen.Home,
    Screen.Search,
    Screen.Builds,
    Screen.Market,
    Screen.Profile
)

// ── Root composable ───────────────────────────────────────────────────────

@Composable
fun nPCsApp(
    gpuViewModel: GPUViewModel,
    buildViewModel: BuildViewModel,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination

            NavigationBar(
                containerColor     = SurfaceContainerHigh.copy(alpha = 0.92f),
                contentColor       = OnSurfaceVariant,
                tonalElevation     = 0.dp,
                modifier           = Modifier.clip(
                    RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                )
            ) {
                navItems.forEach { screen ->
                    val isSelected = currentDestination
                        ?.hierarchy
                        ?.any { it.route == screen.route } == true

                    NavigationBarItem(
                        icon = {
                            Icon(
                                imageVector     = screen.icon,
                                contentDescription = screen.label,
                                modifier        = Modifier.size(24.dp)
                            )
                        },
                        label = {
                            Text(
                                text  = screen.label,
                                style = MaterialTheme.typography.labelSmall
                            )
                        },
                        selected = isSelected,
                        enabled  = screen.enabled,
                        onClick  = {
                            if (screen.enabled) {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState    = true
                                }
                            }
                            // disabled tabs → do nothing (no-op)
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor       = NeonBlue,
                            selectedTextColor       = NeonBlue,
                            indicatorColor          = PrimaryContainer.copy(alpha = 0.20f),
                            unselectedIconColor     = OnSurfaceVariant.copy(alpha = 0.55f),
                            unselectedTextColor     = OnSurfaceVariant.copy(alpha = 0.55f),
                            disabledIconColor       = OnSurfaceVariant.copy(alpha = 0.35f),
                            disabledTextColor       = OnSurfaceVariant.copy(alpha = 0.35f),
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController    = navController,
            startDestination = "welcome_screen",
            modifier         = Modifier.padding(innerPadding)
        ) {
            // ── Builds / Home ────────────────────────────────────────────
            composable("welcome_screen") {
                WelcomeScreen(
                    onStart = {
                        navController.navigate("home_screen"){
                            popUpTo("welcome_screen"){inclusive = true}
                        }
                    }
                )
            }

            // ── Home (pantalla principal) ────────────────────────────────
            composable(Screen.Home.route) {
                HomeScreen(
                    gpuViewModel   = gpuViewModel,
                    buildViewModel = buildViewModel,
                    onStartBuild   = { navController.navigate("builds_screen") },
                    onOpenBuild    = { id -> navController.navigate("build_detail/$id") },
                    onOpenGpu      = { gpuId -> navController.navigate("gpu_detail/$gpuId") },
                    onOpenMarket   = { navController.navigate("market_screen") },
                    onSeeAllBuilds = { navController.navigate("builds_screen") }
                )
            }
            composable(Screen.Builds.route) {     // == "builds_screen"
                BuildsScreen(
                    gpuViewModel  = gpuViewModel,
                    buildViewModel = buildViewModel,
                    onNavigateToDetail = { id ->
                        navController.navigate("build_detail/$id")
                    }
                )
            }


            // ── Build Detail ─────────────────────────────────────────────
            composable(
                "build_detail/{buildId}",
                arguments = listOf(navArgument("buildId") { type = NavType.LongType })
            ) { backStackEntry ->
                val id = backStackEntry.arguments?.getLong("buildId") ?: 0L
                buildViewModel.selectBuild(id)
                BuildDetailScreen(
                    buildId   = id,
                    buildViewModel = buildViewModel,
                    gpuViewModel   = gpuViewModel,
                    onBack = { navController.popBackStack() },
                    onNavigateToComponentDetail = { componentId ->
                        navController.navigate("component_detail/$componentId")
                    },
                    onNavigateToGpuDetail = { gpuId ->
                        navController.navigate("gpu_detail/$gpuId")
                    }
                )
            }


            // ── Component Select ─────────────────────────────────────────
            composable(
                "component_detail/{componentId}",
                arguments = listOf(navArgument("componentId") { type = NavType.LongType })
            ) { backStackEntry ->
                val id = backStackEntry.arguments?.getLong("componentId") ?: 0L
                ComponentSelectScreen(
                    componentId         = id,
                    gpuViewModel        = gpuViewModel,
                    buildViewModel      = buildViewModel,
                    onNavigateToGpuDetail = { gpuId ->
                        navController.navigate("gpu_detail/$gpuId")
                    },
                    onBack = { navController.popBackStack() }
                )
            }

            // ── GPU Detail ───────────────────────────────────────────────
            composable(
                "gpu_detail/{gpuId}",
                arguments = listOf(navArgument("gpuId") { type = NavType.IntType })
            ) { backStackEntry ->
                val gpuId = backStackEntry.arguments?.getInt("gpuId") ?: 0
                GPUDetailScreen(
                    gpuId          = gpuId,
                    gpuViewModel   = gpuViewModel,
                    buildViewModel = buildViewModel,
                    onBack         = { navController.popBackStack() }
                )
            }

            // ── Market Screen ──────────────────────────────────────────
            composable(Screen.Market.route) {
                MarketScreen(
                    onNavigateToSell = {
                        navController.navigate("sell_screen")
                    },
                    onNavigateToDetail = { itemId ->
                        navController.navigate("market_detail/$itemId")
                    }
                )
            }

            // ── Sell Screen (Vender Componente) ────────────────────────
            composable("sell_screen") {
                // Tu pantalla SellScreen ya está creada en tu código
                SellScreen()
            }

            // ── Market Item Detail (Detalle del Producto) ──────────────
            composable("market_detail/{itemId}") { backStackEntry ->
                val itemId = backStackEntry.arguments?.getString("itemId") ?: ""

                // TODO:Aquí irá la pantalla de detalle del producto.
                // Por ahora usamos un placeholder para que no falle la navegación:
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Detalle del producto en construcción. ID: $itemId", color = Color.White)
                }
            }

            // ── Placeholder routes for future screens ────────────────────
            composable(Screen.Search.route) {
                SearchScreen(
                    gpuViewModel = gpuViewModel,
                    onNavigateToGpuDetail = { gpuId -> navController.navigate("gpu_detail/$gpuId") }
                )
            }
            // composable(Screen.Profile.route) { ProfileScreen(...) }
        }
    }
}