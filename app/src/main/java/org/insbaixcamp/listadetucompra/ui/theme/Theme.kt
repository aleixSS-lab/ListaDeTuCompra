package org.insbaixcamp.listadetucompra.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = MintDarkPrimary,
    onPrimary = MintDarkOnPrimary,
    primaryContainer = MintDarkPrimaryContainer,
    onPrimaryContainer = MintDarkOnPrimaryContainer,
    secondary = AmberDarkSecondary,
    onSecondary = AmberDarkOnSecondary,
    secondaryContainer = AmberDarkSecondaryContainer,
    onSecondaryContainer = AmberDarkOnSecondaryContainer,
    background = DeepDarkBackground,
    onBackground = DeepDarkOnBackground,
    surface = DeepDarkSurface,
    onSurface = DeepDarkOnSurface,
    surfaceVariant = DeepDarkSurfaceVariant,
    onSurfaceVariant = DeepDarkOnSurfaceVariant
)

private val LightColorScheme = lightColorScheme(
    primary = FreshGreenPrimary,
    onPrimary = FreshGreenOnPrimary,
    primaryContainer = FreshGreenPrimaryContainer,
    onPrimaryContainer = FreshGreenOnPrimaryContainer,
    secondary = CitrusAmberSecondary,
    onSecondary = CitrusAmberOnSecondary,
    secondaryContainer = CitrusAmberSecondaryContainer,
    onSecondaryContainer = CitrusAmberOnSecondaryContainer,
    background = GroceryBackground,
    onBackground = GroceryOnBackground,
    surface = GrocerySurface,
    onSurface = GroceryOnSurface,
    surfaceVariant = GrocerySurfaceVariant,
    onSurfaceVariant = GroceryOnSurfaceVariant
)

@Composable
fun ListaDeTuCompraTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Usamos por defecto el esquema de la app para mantener el diseño único de la compra
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
