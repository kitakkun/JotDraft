package com.github.kitakkun.noteapp.di

import androidx.navigation.NavController
import com.github.kitakkun.noteapp.ui.page.editor.EditorViewModel
import com.github.kitakkun.noteapp.ui.page.finder.FinderViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { (documentId: String?, navController: NavController) ->
        EditorViewModel(
            documentId = documentId,
            documentRepository = get(),
            navController = navController,
        )
    }
    viewModel {
        FinderViewModel(get())
    }
}
