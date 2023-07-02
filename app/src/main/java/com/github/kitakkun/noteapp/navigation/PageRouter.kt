package com.github.kitakkun.noteapp.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navigation
import com.github.kitakkun.noteapp.editor.editor.EditorPage
import com.github.kitakkun.noteapp.finder.FinderPage
import com.github.kitakkun.noteapp.setting.SettingPage
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun PageRouter(
    navController: NavHostController,
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    Scaffold(
        bottomBar = {
            NABottomNavBar(
                currentRoute = navBackStackEntry?.destination?.route,
                onClickDocumentsTab = {
                    navController.navigate(MainPage.Finder.route) {
                        popUpTo(MainPage.route) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onClickSettingsTab = {
                    navController.navigate(MainPage.Setting.route) {
                        popUpTo(MainPage.route) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }
    ) {
        NavHost(
            navController = navController,
            startDestination = MainPage.route,
            modifier = Modifier
                .padding(it)
                .clipToBounds()
        ) {
            navigation(route = MainPage.route, startDestination = MainPage.Finder.route) {
                composable(route = MainPage.Finder.route) {
                    FinderPage(
                        viewModel = koinViewModel(),
                        navController = navController
                    )
                }
                composable(route = MainPage.Setting.route) {
                    SettingPage(
                        viewModel = koinViewModel(),
                    )
                }
                composable(route = MainPage.Editor.routeWithArgs) {
                    val args = MainPage.Editor.resolveArguments(it)
                    val documentId = args[0] as String?
                    EditorPage(viewModel = koinViewModel { parametersOf(documentId, navController) })
                }
                composable(route = MainPage.Editor.route) {
                    EditorPage(viewModel = koinViewModel { parametersOf(null, navController) })
                }
            }
        }

    }
}
