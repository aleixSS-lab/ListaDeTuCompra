package org.insbaixcamp.listadetucompra

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.firebase.Firebase
import com.google.firebase.database.FirebaseDatabase
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

        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("compras")
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

@Composable
fun PantallaAnadirProducto() {
    var textoProducto by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = textoProducto,
            onValueChange = { textoProducto = it },
            label = { Text("Nombre del producto") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                if (textoProducto.isNotBlank()) {
                    // Obtenemos la referencia a Firebase y guardamos el producto
                    val database = FirebaseDatabase.getInstance()
                    val myRef = database.getReference("compras")

                    // Creamos un ID único para el producto
                    val idUnico = myRef.push().key
                    if (idUnico != null) {
                        myRef.child(idUnico).setValue(textoProducto)
                        textoProducto = "" // Limpiamos el cajón de texto
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Añadir a la lista")
        }
    }
}
