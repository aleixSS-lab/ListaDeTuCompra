package org.insbaixcamp.listadetucompra

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import org.insbaixcamp.listadetucompra.ui.auth.AuthUiState
import org.insbaixcamp.listadetucompra.ui.auth.AuthViewModel
import org.insbaixcamp.listadetucompra.ui.screen.AppNavigation
import org.insbaixcamp.listadetucompra.ui.screen.LoginScreen
import org.insbaixcamp.listadetucompra.ui.theme.AppLanguage
import org.insbaixcamp.listadetucompra.ui.theme.ListaDeTuCompraTheme

class MainActivity : ComponentActivity() {

    // ViewModel encargado de gestionar el inicio de sesión.
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            var isDarkTheme by remember { mutableStateOf(false) }
            var currentLanguage by remember { mutableStateOf(AppLanguage.ES) }

            ListaDeTuCompraTheme(darkTheme = isDarkTheme) {

                val uiState by authViewModel.uiState.collectAsState()
                val context = LocalContext.current
                val webClientId = stringResource(id = R.string.default_web_client_id)

                when (val state = uiState) {

                    is AuthUiState.Success -> {
                        AppNavigation(
                            isDarkTheme = isDarkTheme,
                            onToggleDarkTheme = { isDarkTheme = !isDarkTheme },
                            currentLanguage = currentLanguage,
                            onChangeLanguage = { newLang -> currentLanguage = newLang },
                            onSignOut = { authViewModel.signOut() }
                        )
                    }

                    else -> {
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
