
package org.insbaixcamp.listadetucompra

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import org.insbaixcamp.listadetucompra.ui.components.BottomBar
import org.insbaixcamp.listadetucompra.ui.screen.MainScreen
import org.insbaixcamp.listadetucompra.ui.screen.Routes
import org.insbaixcamp.listadetucompra.ui.screen.ShoppingListScreen
import org.insbaixcamp.listadetucompra.ui.screen.TagsScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            if (currentRoute == Routes.HOME ||
                currentRoute == Routes.TAGS
            ) {
                BottomBar(
                    selectedRoute = currentRoute,
                    onNavigate = { route ->
                        if (route != currentRoute) {
                            navController.navigate(route) {
                                popUpTo(Routes.HOME) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Routes.HOME,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Routes.HOME) {
                MainScreen(
                    onListClick = { listId ->
                        navController.navigate(Routes.list(listId))
                    },
                    onTagsClick = {
                        navController.navigate(Routes.TAGS)
                    }
                )
            }

            composable(Routes.TAGS) {
                TagsScreen()
            }

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
                    .orEmpty()

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
