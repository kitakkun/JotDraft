package com.github.kitakkun.noteapp.di

import androidx.navigation.NavController
import com.github.kitakkun.noteapp.ui.page.editor.EditHistoryManager
import com.github.kitakkun.noteapp.ui.page.editor.EditorViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val appModule = module {
    viewModel { (documentId: String?, navController: NavController) ->
        EditorViewModel(
            documentId = documentId,
            documentRepository = get(),
            navController = navController,
            historyManager = get(),
        )
    }
    factoryOf(::EditHistoryManager)
}
