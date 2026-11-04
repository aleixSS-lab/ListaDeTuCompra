package org.insbaixcamp.listadetucompra.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.insbaixcamp.listadetucompra.ui.auth.AuthUiState

// Dos tonos de Azul: Azul Eléctrico (#2563EB) y Azul Hielo/Celeste Pastel (#DBEAFE / #EFF6FF)
private val ElectricBlue = Color(0xFF2563EB)
private val IceSkyBlue = Color(0xFFDBEAFE)
private val LightIceBlueContainer = Color(0xFFEFF6FF)
private val DarkBlueText = Color(0xFF1E40AF)

@Composable
fun LoginScreen(
    uiState: AuthUiState,
    modifier: Modifier = Modifier,
    onGoogleSignInClick: () -> Unit = {}
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = Color.White
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Icono destacado en dos tonos de azul (Círculo Azul Celeste + Cesta Azul Eléctrico)
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(
                        color = IceSkyBlue,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = null,
                    tint = ElectricBlue,
                    modifier = Modifier.size(50.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Insignia estilizada en Azul Hielo con texto Azul Oscuro
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = IceSkyBlue
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "🛒 Lista De Tu Compra",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkBlueText
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Bienvenid@",
                style = MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                color = ElectricBlue
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Inicia sesión para guardar y sincronizar tus listas del supermercado",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(36.dp))

            // Tarjeta de inicio de sesión con combinación de los dos tonos de azul
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(1.5.dp, Color(0xFFBFDBFE)), // Borde Azul Celeste
                colors = CardDefaults.cardColors(
                    containerColor = LightIceBlueContainer // Fondo Azul Hielo muy suave
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    when (uiState) {
                        is AuthUiState.Loading -> {
                            CircularProgressIndicator(color = ElectricBlue)
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Iniciando sesión...",
                                style = MaterialTheme.typography.bodyMedium,
                                color = DarkBlueText
                            )
                        }

                        else -> {
                            Button(
                                onClick = onGoogleSignInClick,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(54.dp),
                                shape = RoundedCornerShape(16.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = ElectricBlue, // Botón Azul Eléctrico
                                    contentColor = Color.White
                                )
                            ) {
                                Text(
                                    text = "Iniciar sesión con Google",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            if (uiState is AuthUiState.Error) {
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = uiState.message,
                                    color = MaterialTheme.colorScheme.error,
                                    style = MaterialTheme.typography.bodyMedium,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun LoginScreenPreview() {
    LoginScreen(
        uiState = AuthUiState.Idle
    )
}
