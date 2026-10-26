package org.insbaixcamp.listadetucompra.ui.screen

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

//El controlador para poder cambiar de pantallas con la barra de navegacion de la parte inferior
//remember hace que recuerde que navContoller existe en toda la app
@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    val backStackEntry by navController.currentBackStackEntryAsState() // el currentBack... es para saber en que pagina estas |la funcion pregunta al navController donde estamos y se devuelve en un estado (State)
    val rutaActual = backStackEntry?.destination?.route //Guardar donde estamos

//    El esqueleto de la pantalla
    Scaffold(
        modifier = Modifier.fillMaxWidth(),
        bottomBar = {
            BottomBar(
                rutaActual = rutaActual,
                onItemClick = { ruta ->
                    navController.navigate(ruta) {
                        popUpTo(Routes.MAIN) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Routes.MAIN,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Routes.MAIN) { MainScreen() }

            composable(Routes.LABELS) { TagsScreen() }

        }
    }
}
