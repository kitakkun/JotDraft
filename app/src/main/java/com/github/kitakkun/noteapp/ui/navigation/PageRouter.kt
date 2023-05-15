package com.github.kitakkun.noteapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
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
        startDestination = "noteFinder",
    ) {
        composable("noteList") {
//            NoteListPage()
        }
        composable("noteEdit") {
            NoteEditorPage(viewModel = koinViewModel() { parametersOf() })
        }
        composable("noteFinder") {
            FinderPage(viewModel = koinViewModel())
        }
    }
}
