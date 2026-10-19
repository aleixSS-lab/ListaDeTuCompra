package org.insbaixcamp.listadetucompra

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import org.insbaixcamp.listadetucompra.ui.auth.AuthUiState
import org.insbaixcamp.listadetucompra.ui.auth.AuthViewModel
import org.insbaixcamp.listadetucompra.ui.screen.LoginScreen
import org.insbaixcamp.listadetucompra.ui.screen.MainScreenContent
import org.insbaixcamp.listadetucompra.ui.theme.ListaDeTuCompraTheme


class MainActivity : ComponentActivity() {

    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val db = Firebase.firestore
        db.collection("test").add(mapOf("hola" to "mundo"))
            .addOnSuccessListener { Log.d("FIREBASE", "Conectado: ${it.id}") }
            .addOnFailureListener { Log.e("FIREBASE", "Error", it) }
        enableEdgeToEdge()
        setContent {
            ListaDeTuCompraTheme {
                val uiState by authViewModel.uiState.collectAsState()
                val context = LocalContext.current
                val webClientId = stringResource(id = R.string.default_web_client_id)

                when (val state = uiState) {
                    is AuthUiState.Success -> {
                        MainScreenContent(
                            userDisplayName = state.user.displayName ?: state.user.email ?: "Usuario",
                            onSignOut = { authViewModel.signOut() }
                        )
                    }
                    else -> {
                        LoginScreen(
                            uiState = state,
                            onGoogleSignInClick = {
                                authViewModel.signInWithGoogle(context, webClientId)
                            }
                        )
                    }
                }
            }
        }
    }
}
