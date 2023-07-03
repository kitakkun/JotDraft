package com.github.kitakkun.noteapp.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notes
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.github.kitakkun.noteapp.customview.preview.PreviewContainer

@Composable
fun NABottomNavBar(
    currentRoute: String?,
    onClickDocumentsTab: () -> Unit,
    onClickSettingsTab: () -> Unit,
) {
    val shouldVisible = currentRoute == MainPage.Finder.route || currentRoute == MainPage.Setting.route
    AnimatedVisibility(
        visible = shouldVisible,
        enter = expandVertically(),
        exit = shrinkVertically(),
    ) {
        NavigationBar {
            NavigationBarItem(
                selected = currentRoute == MainPage.Finder.route,
                onClick = onClickDocumentsTab,
                icon = {
                    Icon(imageVector = Icons.Default.Notes, contentDescription = null)
                },
                label = { Text(text = "Documents") }
            )
            NavigationBarItem(
                selected = currentRoute == MainPage.Setting.route,
                onClick = onClickSettingsTab,
                icon = {
                    Icon(imageVector = Icons.Default.Settings, contentDescription = null)
                },
                label = { Text(text = "Settings") }
            )
        }
    }
}

@Preview
@Composable
private fun NABottomNavBarPreview() = PreviewContainer {
    NABottomNavBar(
        currentRoute = MainPage.Finder.route,
        onClickDocumentsTab = {},
        onClickSettingsTab = {},
    )
}
