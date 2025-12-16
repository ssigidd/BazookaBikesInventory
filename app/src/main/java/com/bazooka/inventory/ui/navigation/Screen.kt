package com.bazooka.inventory.ui.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object BikePartsList : Screen("bike_parts")
    object BikePartDetail : Screen("bike_part/{bikePartId}") {
        fun createRoute(bikePartId: Long) = "bike_part/$bikePartId"
    }
    object AddBikePart : Screen("add_bike_part")
    object EditBikePart : Screen("edit_bike_part/{bikePartId}") {
        fun createRoute(bikePartId: Long) = "edit_bike_part/$bikePartId"
    }
    object LowOnStock : Screen("low_on_stock")
    object ProjectsList : Screen("projects")
    object ProjectDetail : Screen("project/{projectId}") {
        fun createRoute(projectId: Long) = "project/$projectId"
    }
    object AddProject : Screen("add_project")
    object EditProject : Screen("edit_project/{projectId}") {
        fun createRoute(projectId: Long) = "edit_project/$projectId"
    }
}
