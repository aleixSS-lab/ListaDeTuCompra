package org.insbaixcamp.listadetucompra.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import org.insbaixcamp.listadetucompra.ui.screen.Routes
import org.insbaixcamp.listadetucompra.ui.theme.AppLanguage
import org.insbaixcamp.listadetucompra.ui.theme.Strings

@Composable
fun BottomBar(
    selectedRoute: String,
    language: AppLanguage = AppLanguage.ES,
    onNavigate: (String) -> Unit
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 6.dp
    ) {
        NavigationBarItem(
            selected = selectedRoute == Routes.HOME,
            onClick = {
                onNavigate(Routes.HOME)
            },
            icon = {
                Icon(
                    imageVector = Icons.Filled.Home,
                    contentDescription = Strings.navHome(language)
                )
            },
            label = {
                Text(Strings.navHome(language))
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                selectedTextColor = MaterialTheme.colorScheme.primary,
                indicatorColor = MaterialTheme.colorScheme.primaryContainer
            )
        )

        NavigationBarItem(
            selected = selectedRoute == Routes.TAGS,
            onClick = {
                onNavigate(Routes.TAGS)
            },
            icon = {
                Icon(
                    imageVector = Icons.Filled.LocalOffer,
                    contentDescription = Strings.navTags(language)
                )
            },
            label = {
                Text(Strings.navTags(language))
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                selectedTextColor = MaterialTheme.colorScheme.primary,
                indicatorColor = MaterialTheme.colorScheme.primaryContainer
            )
        )
    }
}
