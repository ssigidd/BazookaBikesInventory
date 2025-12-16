package com.bazooka.inventory.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.bazooka.inventory.ui.screens.*

@Composable
fun BazookaNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToBikeParts = { navController.navigate(Screen.BikePartsList.route) },
                onNavigateToProjects = { navController.navigate(Screen.ProjectsList.route) },
                onNavigateToLowStock = { navController.navigate(Screen.LowOnStock.route) }
            )
        }

        composable(Screen.LowOnStock.route) {
            LowOnStockScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToDetail = { bikePartId ->
                    navController.navigate(Screen.BikePartDetail.createRoute(bikePartId))
                }
            )
        }

        composable(Screen.BikePartsList.route) {
            BikePartsListScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToDetail = { bikePartId ->
                    navController.navigate(Screen.BikePartDetail.createRoute(bikePartId))
                },
                onNavigateToAdd = { navController.navigate(Screen.AddBikePart.route) }
            )
        }

        composable(
            route = Screen.BikePartDetail.route,
            arguments = listOf(navArgument("bikePartId") { type = NavType.LongType })
        ) { backStackEntry ->
            val bikePartId = backStackEntry.arguments?.getLong("bikePartId") ?: return@composable
            BikePartDetailScreen(
                bikePartId = bikePartId,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToEdit = { id ->
                    navController.navigate(Screen.EditBikePart.createRoute(id))
                }
            )
        }

        composable(Screen.AddBikePart.route) {
            AddBikePartScreen(
                onNavigateBack = { navController.popBackStack() },
                onSaved = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.EditBikePart.route,
            arguments = listOf(navArgument("bikePartId") { type = NavType.LongType })
        ) { backStackEntry ->
            val bikePartId = backStackEntry.arguments?.getLong("bikePartId") ?: return@composable
            EditBikePartScreen(
                bikePartId = bikePartId,
                onNavigateBack = { navController.popBackStack() },
                onSaved = { navController.popBackStack() }
            )
        }

        composable(Screen.ProjectsList.route) {
            ProjectsListScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToDetail = { projectId ->
                    navController.navigate(Screen.ProjectDetail.createRoute(projectId))
                },
                onNavigateToAdd = { navController.navigate(Screen.AddProject.route) }
            )
        }

        composable(
            route = Screen.ProjectDetail.route,
            arguments = listOf(navArgument("projectId") { type = NavType.LongType })
        ) { backStackEntry ->
            val projectId = backStackEntry.arguments?.getLong("projectId") ?: return@composable
            ProjectDetailScreen(
                projectId = projectId,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToEdit = { id ->
                    navController.navigate(Screen.EditProject.createRoute(id))
                }
            )
        }

        composable(Screen.AddProject.route) {
            AddProjectScreen(
                onNavigateBack = { navController.popBackStack() },
                onSaved = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.EditProject.route,
            arguments = listOf(navArgument("projectId") { type = NavType.LongType })
        ) { backStackEntry ->
            val projectId = backStackEntry.arguments?.getLong("projectId") ?: return@composable
            EditProjectScreen(
                projectId = projectId,
                onNavigateBack = { navController.popBackStack() },
                onSaved = { navController.popBackStack() }
            )
        }
    }
}
