
package org.insbaixcamp.listadetucompra.ui.screen

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.HOME
    ) {
        // Pantalla principal: muestra las listas de la compra
        composable(route = Routes.HOME) {
            MainScreen(
                onListClick = { listId ->
                    navController.navigate(Routes.list(listId))
                },
                onTagsClick = {
                    navController.navigate(Routes.TAGS)
                }
            )
        }

        // Pantalla de etiquetas
        composable(route = Routes.TAGS) {
            TagsScreen()
        }

        // Pantalla de una lista concreta
        composable(
            route = Routes.LIST,
            arguments = listOf(
                navArgument("listId") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->

            val listId = backStackEntry.arguments
                ?.getString("listId")

            if (listId != null) {
                ShoppingListScreen(
                    listId = listId,
                    onBack = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}
