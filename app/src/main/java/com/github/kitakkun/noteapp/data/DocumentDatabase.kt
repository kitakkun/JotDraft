package com.github.kitakkun.noteapp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [DocumentEntity::class], version = 1)
@TypeConverters(DocumentConverter::class)
abstract class DocumentDatabase : RoomDatabase() {
    abstract fun documentDao(): DocumentDao
}