package com.github.kitakkun.noteapp.di

import androidx.room.Room
import com.github.kitakkun.noteapp.data.DocumentDatabase
import com.github.kitakkun.noteapp.data.DocumentRepository
import com.github.kitakkun.noteapp.ui.page.editor.NoteEditorViewModel
import com.github.kitakkun.noteapp.ui.page.finder.FinderViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModelOf
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
    viewModelOf(::NoteEditorViewModel)
    viewModelOf(::FinderViewModel)
}
