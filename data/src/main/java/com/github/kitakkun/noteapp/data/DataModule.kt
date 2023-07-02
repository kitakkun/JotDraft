package com.github.kitakkun.noteapp.data

import androidx.room.Room
import com.github.kitakkun.noteapp.data.repository.DocumentRepository
import com.github.kitakkun.noteapp.data.room.DOCUMENT_MIGRATION_V1_V2
import com.github.kitakkun.noteapp.data.room.DocumentDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val dataModule = module {
    singleOf(::DocumentRepository)
    single {
        Room.databaseBuilder(
            context = androidContext(),
            klass = DocumentDatabase::class.java,
            name = "document_database",
        ).addMigrations(DOCUMENT_MIGRATION_V1_V2).build()
    }
    single { get<DocumentDatabase>().documentDao() }
}