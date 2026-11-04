package org.insbaixcamp.listadetucompra.ui.theme

import android.os.Build
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = DarkBluePrimary,
    onPrimary = DarkBlueOnPrimary,
    primaryContainer = DarkBluePrimaryContainer,
    onPrimaryContainer = DarkBlueOnPrimaryContainer,
    secondary = DarkIndigoSecondary,
    onSecondary = DarkIndigoOnSecondary,
    secondaryContainer = DarkIndigoSecondaryContainer,
    onSecondaryContainer = DarkIndigoOnSecondaryContainer,
    background = DarkCleanBackground,
    onBackground = DarkCleanOnBackground,
    surface = DarkCleanSurface,
    onSurface = DarkCleanOnSurface,
    surfaceVariant = DarkCleanSurfaceVariant,
    onSurfaceVariant = DarkCleanOnSurfaceVariant
)

private val LightColorScheme = lightColorScheme(
    primary = ElectricBluePrimary,
    onPrimary = ElectricBlueOnPrimary,
    primaryContainer = ElectricBluePrimaryContainer,
    onPrimaryContainer = ElectricBlueOnPrimaryContainer,
    secondary = SoftIndigoSecondary,
    onSecondary = SoftIndigoOnSecondary,
    secondaryContainer = SoftIndigoSecondaryContainer,
    onSecondaryContainer = SoftIndigoOnSecondaryContainer,
    background = CleanBackground,
    onBackground = CleanOnBackground,
    surface = CleanSurface,
    onSurface = CleanOnSurface,
    surfaceVariant = CleanSurfaceVariant,
    onSurfaceVariant = CleanOnSurfaceVariant
)

@Composable
fun ListaDeTuCompraTheme(
    darkTheme: Boolean = false,
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
