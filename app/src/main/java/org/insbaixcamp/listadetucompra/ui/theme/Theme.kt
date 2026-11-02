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
    primary = DarkPrimaryEmerald,
    onPrimary = DarkOnPrimaryEmerald,
    primaryContainer = DarkPrimaryEmeraldContainer,
    onPrimaryContainer = DarkOnPrimaryEmeraldContainer,
    secondary = DarkAccentIndigo,
    onSecondary = DarkOnAccentIndigo,
    secondaryContainer = DarkAccentIndigoContainer,
    onSecondaryContainer = DarkOnAccentIndigoContainer,
    background = DarkAppBackground,
    onBackground = DarkAppOnBackground,
    surface = DarkAppSurface,
    onSurface = DarkAppOnSurface,
    surfaceVariant = DarkAppSurfaceVariant,
    onSurfaceVariant = DarkAppOnSurfaceVariant
)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryEmerald,
    onPrimary = OnPrimaryEmerald,
    primaryContainer = PrimaryEmeraldContainer,
    onPrimaryContainer = OnPrimaryEmeraldContainer,
    secondary = AccentIndigo,
    onSecondary = OnAccentIndigo,
    secondaryContainer = AccentIndigoContainer,
    onSecondaryContainer = OnAccentIndigoContainer,
    background = AppBackground,
    onBackground = AppOnBackground,
    surface = AppSurface,
    onSurface = AppOnSurface,
    surfaceVariant = AppSurfaceVariant,
    onSurfaceVariant = AppOnSurfaceVariant
)

@Composable
fun ListaDeTuCompraTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
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
