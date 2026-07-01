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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
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
import com.example.npcsapp.ui.screens.ComponentDetailScreen
import com.example.npcsapp.ui.screens.HomeScreen
import com.example.npcsapp.ui.screens.MarketScreen
import com.example.npcsapp.ui.screens.MarketDetailScreen
import com.example.npcsapp.ui.screens.ChatListScreen
import com.example.npcsapp.ui.screens.ChatScreen
import com.example.npcsapp.ui.screens.ProfileScreen
import com.example.npcsapp.ui.screens.SearchScreen
import com.example.npcsapp.ui.screens.SellScreen
import com.example.npcsapp.ui.screens.WelcomeScreen
import com.example.npcsapp.ui.screens.auth.AuthViewModel
import com.example.npcsapp.ui.screens.auth.LoginScreen
import com.example.npcsapp.ui.screens.auth.RegisterScreen
import com.example.npcsapp.ui.theme.NeonBlue
import com.example.npcsapp.ui.theme.OnSurfaceVariant
import com.example.npcsapp.ui.theme.PrimaryContainer
import com.example.npcsapp.ui.theme.SurfaceContainerHigh
import com.example.npcsapp.viewmodel.BuildViewModel
import com.example.npcsapp.viewmodel.ComponentViewModel
import com.example.npcsapp.viewmodel.MarketViewModel
import com.example.npcsapp.viewmodel.ChatViewModel
import kotlinx.coroutines.launch

sealed class Screen(
    val route: String,
    val label: String,
    val icon: ImageVector,
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
        icon    = Icons.Default.Search
    )
    object Builds : Screen(
        route = "builds_screen",
        label = "Build",
        icon  = Icons.Default.Build
    )
    object Market : Screen(
        route   = "market_screen",
        label   = "Market",
        icon    = Icons.Default.ShoppingCart
    )
    object Profile : Screen(
        route   = "profile_screen",
        label   = "Profile",
        icon    = Icons.Default.Person
    )
}

private val navItems = listOf(
    Screen.Home,
    Screen.Search,
    Screen.Builds,
    Screen.Market,
    Screen.Profile
)

@Composable
fun nPCsApp(
    componentViewModel: ComponentViewModel,
    buildViewModel: BuildViewModel,
    authViewModel: AuthViewModel,
    marketViewModel: MarketViewModel,
    chatViewModel: ChatViewModel,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val scope = rememberCoroutineScope()
    
    val currentUser by authViewModel.user.collectAsState()

    // Inicializar ChatViewModel con el ID del usuario actual
    LaunchedEffect(currentUser) {
        currentUser?.let {
            chatViewModel.initViewModel(it.uid)
        }
    }

    val showBottomBar = currentDestination?.route !in listOf("welcome_screen", "login_screen", "register_screen")

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
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
        }
    ) { innerPadding ->
        NavHost(
            navController    = navController,
            startDestination = if (currentUser != null) Screen.Home.route else "welcome_screen",
            modifier         = Modifier.padding(innerPadding)
        ) {
            composable("welcome_screen") {
                WelcomeScreen(
                    onStart = {
                        navController.navigate("login_screen")
                    }
                )
            }

            composable("login_screen") {
                LoginScreen(
                    viewModel = authViewModel,
                    onNavigateToRegister = { navController.navigate("register_screen") },
                    onLoginSuccess = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo("login_screen") { inclusive = true }
                        }
                    }
                )
            }

            composable("register_screen") {
                RegisterScreen(
                    viewModel = authViewModel,
                    onNavigateToLogin = { navController.popBackStack() },
                    onRegisterSuccess = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo("register_screen") { inclusive = true }
                        }
                    }
                )
            }

            composable(Screen.Home.route) {
                HomeScreen(
                    componentViewModel = componentViewModel,
                    buildViewModel = buildViewModel,
                    onStartBuild = { navController.navigate("builds_screen") },
                    onOpenBuild = { id -> navController.navigate("build_detail/$id") },
                    onOpenComponent = { type, id -> navController.navigate("component_detail/$type/$id") },
                    onNavigateToSearch = { category -> navController.navigate("search_screen?category=$category") },
                    onOpenMarket = { navController.navigate("market_screen") },
                    onSeeAllBuilds = { navController.navigate("builds_screen") }
                )
            }

            composable(Screen.Builds.route) {
                BuildsScreen(
                    componentViewModel  = componentViewModel,
                    buildViewModel = buildViewModel,
                    onNavigateToDetail = { id ->
                        navController.navigate("build_detail/$id")
                    }
                )
            }

            composable(
                "build_detail/{buildId}",
                arguments = listOf(navArgument("buildId") { type = NavType.LongType })
            ) { backStackEntry ->
                val id = backStackEntry.arguments?.getLong("buildId") ?: 0L
                buildViewModel.selectBuild(id)
                BuildDetailScreen(
                    buildId = id,
                    buildViewModel = buildViewModel,
                    componentViewModel = componentViewModel,
                    onBack = { navController.popBackStack() },
                    onNavigateToComponentSelect = { type ->
                        navController.navigate("component_select/$type")
                    },
                    onNavigateToComponentDetail = { type, componentId ->
                        navController.navigate("component_detail/$type/$componentId")
                    }
                )
            }

            composable(
                "component_select/{componentType}",
                arguments = listOf(navArgument("componentType") { type = NavType.StringType })
            ) { backStackEntry ->
                val componentType = backStackEntry.arguments?.getString("componentType") ?: "GPU"
                ComponentSelectScreen(
                    componentType = componentType,
                    componentViewModel = componentViewModel,
                    buildViewModel = buildViewModel,
                    onNavigateToDetail = { id -> navController.navigate("component_detail/$componentType/$id") },
                    onBack = { navController.popBackStack() }
                )
            }

            composable(
                "component_detail/{componentType}/{componentId}",
                arguments = listOf(
                    navArgument("componentType") { type = NavType.StringType },
                    navArgument("componentId") { type = NavType.IntType }
                )
            ) { backStackEntry ->
                val componentType = backStackEntry.arguments?.getString("componentType") ?: "GPU"
                val componentId = backStackEntry.arguments?.getInt("componentId") ?: 0
                ComponentDetailScreen(
                    componentType = componentType,
                    componentId = componentId,
                    componentViewModel = componentViewModel,
                    buildViewModel = buildViewModel,
                    onBack = { navController.popBackStack() }
                )
            }

            composable(Screen.Market.route) {
                MarketScreen(
                    viewModel = marketViewModel,
                    onNavigateToSell = {
                        navController.navigate("sell_screen")
                    },
                    onNavigateToDetail = { itemId ->
                        navController.navigate("market_detail/$itemId")
                    },
                    onNavigateToChatList = {
                        navController.navigate("chat_list")
                    },
                    onStartChat = { otherId: String, otherName: String ->
                        scope.launch {
                            val myId = currentUser?.uid ?: ""
                            val myName = currentUser?.displayName ?: "Usuario"
                            val chatId = chatViewModel.startChatWith(myId, myName, otherId, otherName)
                            navController.navigate("chat_screen/$chatId")
                        }
                    }
                )
            }

            composable("sell_screen") {
                SellScreen(
                    marketViewModel = marketViewModel,
                    authViewModel = authViewModel,
                    onBack = { navController.popBackStack() }
                )
            }

            composable(
                "market_detail/{itemId}",
                arguments = listOf(navArgument("itemId") { type = NavType.LongType })
            ) { backStackEntry ->
                val itemId = backStackEntry.arguments?.getLong("itemId") ?: 0L
                MarketDetailScreen(
                    itemId = itemId,
                    marketViewModel = marketViewModel,
                    onBack = { navController.popBackStack() },
                    onStartChat = { otherId: String, otherName: String ->
                        scope.launch {
                            val myId = currentUser?.uid ?: ""
                            val myName = currentUser?.displayName ?: "Usuario"
                            val chatId = chatViewModel.startChatWith(myId, myName, otherId, otherName)
                            navController.navigate("chat_screen/$chatId")
                        }
                    }
                )
            }

            composable("chat_list") {
                ChatListScreen(
                    viewModel = chatViewModel,
                    onNavigateToChat = { chatId ->
                        navController.navigate("chat_screen/$chatId")
                    },
                    onBack = { navController.popBackStack() }
                )
            }

            composable(
                "chat_screen/{chatId}",
                arguments = listOf(navArgument("chatId") { type = NavType.StringType })
            ) { backStackEntry ->
                val chatId = backStackEntry.arguments?.getString("chatId") ?: ""
                val myId = currentUser?.uid ?: ""
                ChatScreen(
                    chatId = chatId,
                    myUserId = myId,
                    viewModel = chatViewModel,
                    onBack = { navController.popBackStack() }
                )
            }

//            composable(Screen.Search.route) {
//                SearchScreen(
//                    componentViewModel = componentViewModel,
//                    onNavigateToComponentDetail = { type, id ->
//                        navController.navigate("component_detail/$type/$id")
//                    }
//                )
//            }

            composable(
                "search_screen?category={category}",
                arguments = listOf(navArgument("category") {
                    type = NavType.StringType
                    defaultValue = "GPUs"
                })
            ) { backStackEntry ->
                val category = backStackEntry.arguments?.getString("category") ?: "GPUs"
                SearchScreen(
                    componentViewModel = componentViewModel,
                    initialCategory = category,
                    onNavigateToComponentDetail = { type, id ->
                        navController.navigate("component_detail/$type/$id")
                    }
                )
            }

            composable(Screen.Profile.route) {
                ProfileScreen(
                    userName = currentUser?.displayName,
                    userEmail = currentUser?.email,
                    onLogout = {
                        authViewModel.logout()
                        navController.navigate("login_screen") {
                            popUpTo(0)
                        }
                    },
                    onNavigateToMessages = {
                        navController.navigate("chat_list")
                    }
                )
            }
        }
    }
}
