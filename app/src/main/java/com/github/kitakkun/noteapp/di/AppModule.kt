package com.github.kitakkun.noteapp.di

import androidx.navigation.NavController
import androidx.room.Room
import com.github.kitakkun.noteapp.data.DocumentDatabase
import com.github.kitakkun.noteapp.data.DocumentRepository
import com.github.kitakkun.noteapp.ui.page.editor.EditorViewModel
import com.github.kitakkun.noteapp.ui.page.finder.FinderViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val appModule = module {
    singleOf(::DocumentRepository)
    single {
        Room.databaseBuilder(
            context = androidContext(),
            klass = DocumentDatabase::class.java,
            name = "document_database",
        ).build()
    }
    single { get<DocumentDatabase>().documentDao() }
    viewModel { (documentId: String?, navController: NavController) ->
        EditorViewModel(
            documentId = documentId,
            documentRepository = get(),
            navController = navController,
        )
    }
    viewModel { (navController: NavController) ->
        FinderViewModel(
            documentRepository = get(),
            navController = navController
        )
    }
}
