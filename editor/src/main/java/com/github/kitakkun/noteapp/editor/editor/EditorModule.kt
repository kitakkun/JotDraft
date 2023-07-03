package com.github.kitakkun.noteapp.editor.editor

import androidx.navigation.NavController
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val editorModule = module {
    viewModel { (documentId: String?, navController: NavController) ->
        EditorViewModel(
            documentId = documentId,
            documentRepository = get(),
            navController = navController,
            historyManager = get(),
            settingDataStore = get(),
        )
    }
    factoryOf(::EditHistoryManager)
}
