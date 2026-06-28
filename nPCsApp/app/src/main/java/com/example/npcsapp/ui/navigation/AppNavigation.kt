package com.example.npcsapp.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.npcsapp.ui.screens.BuildDetailScreen
import com.example.npcsapp.ui.screens.BuildsScreen
import com.example.npcsapp.ui.screens.ComponentSelectScreen
import com.example.npcsapp.ui.screens.GPUDetailScreen
import com.example.npcsapp.viewmodel.BuildViewModel
import com.example.npcsapp.viewmodel.GPUViewModel

@Composable
fun nPCsApp(
    gpuViewModel: GPUViewModel,
    buildViewModel: BuildViewModel,
    modifier: Modifier = Modifier
){
    val navController = rememberNavController()

    NavHost(navController=navController, startDestination = "builds_screen"){
        composable("builds_screen"){
            BuildsScreen(
                gpuViewModel = gpuViewModel,
                buildViewModel = buildViewModel,
                onNavigateToDetail = { id ->
                    navController.navigate("build_detail/${id}")
                }
            )
        }

//        composable("add_build") {
//            AddBuildScreen(
//                viewModel = viewModel,
//                onBack = {navController.popBackStack()}
//            )
//        }

        composable("build_detail/{buildId}",
            arguments = listOf(navArgument("buildId") {
                type = NavType.LongType })
        ){ backStackEntry ->

            val id = backStackEntry
                .arguments?.getLong("buildId") ?: 0

            buildViewModel.selectBuild(id)

            BuildDetailScreen(
                buildId = id,
                buildViewModel = buildViewModel,
                gpuViewModel = gpuViewModel,
                onBack = { navController.popBackStack() },
                onNavigateToComponentDetail = { componentId ->
                    navController.navigate("component_detail/$componentId")
                }
            )
        }

        composable("component_detail/{componentId}",
            arguments = listOf(navArgument("componentId") {
                type = NavType.LongType })
        ){ backStackEntry ->

            val id = backStackEntry
                .arguments?.getLong("componentId") ?: 0

                ComponentSelectScreen(
                    componentId = id,
                    gpuViewModel = gpuViewModel,
                    buildViewModel = buildViewModel,
                    onBack = {navController.popBackStack()}
                )
        }


    }
}