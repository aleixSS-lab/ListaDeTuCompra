
package org.insbaixcamp.listadetucompra

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import org.insbaixcamp.listadetucompra.ui.auth.AuthUiState
import org.insbaixcamp.listadetucompra.ui.auth.AuthViewModel
//import org.insbaixcamp.listadetucompra.ui.screen.AppNavigation
import org.insbaixcamp.listadetucompra.ui.screen.LoginScreen
import org.insbaixcamp.listadetucompra.ui.theme.ListaDeTuCompraTheme

class MainActivity : ComponentActivity() {

    // ViewModel encargado de gestionar el inicio de sesión.
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Permite que la aplicación dibuje contenido detrás
        // de las barras del sistema cuando corresponda.
        enableEdgeToEdge()

        setContent {
            ListaDeTuCompraTheme {

                // Observamos el estado del inicio de sesión.
                val uiState by authViewModel.uiState.collectAsState()

                // Necesitamos el contexto para iniciar sesión con Google.
                val context = LocalContext.current

                // Identificador de cliente web configurado en Firebase.
                val webClientId = stringResource(
                    id = R.string.default_web_client_id
                )

                // Elegimos qué pantalla mostrar según el estado.
                when (val state = uiState) {

                    is AuthUiState.Success -> {
                        // El usuario ha iniciado sesión.
                        AppNavigation()
                    }

                    else -> {
                        // El usuario todavía no ha iniciado sesión,
                        // está cargando o ha ocurrido un error.
                        LoginScreen(
                            uiState = state,
                            onGoogleSignInClick = {
                                authViewModel.signInWithGoogle(
                                    context = context,
                                    webClientId = webClientId
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}
