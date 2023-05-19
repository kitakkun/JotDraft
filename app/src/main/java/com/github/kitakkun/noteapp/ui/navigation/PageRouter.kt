package com.github.kitakkun.noteapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.github.kitakkun.noteapp.ui.page.editor.NoteEditorPage
import com.github.kitakkun.noteapp.ui.page.finder.FinderPage
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun PageRouter(
    navController: NavHostController,
) {
    NavHost(
        navController = navController,
        startDestination = MainPage.route,
    ) {
        navigation(route = MainPage.route, startDestination = MainPage.Finder.route) {
            composable(route = MainPage.Finder.route) {
                FinderPage(viewModel = koinViewModel())
            }
            composable(route = MainPage.Editor.routeWithArgs) {
                val args = MainPage.Editor.resolveArguments(it)
                val documentId = args[0] as String?
                NoteEditorPage(viewModel = koinViewModel { parametersOf(documentId) })
            }
            composable(route = MainPage.Editor.route) {
                NoteEditorPage(viewModel = koinViewModel { parametersOf(null) })
            }
        }
    }
}
