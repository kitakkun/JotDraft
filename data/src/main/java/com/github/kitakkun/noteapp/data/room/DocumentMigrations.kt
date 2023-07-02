package com.github.kitakkun.noteapp.data.room

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val DOCUMENT_MIGRATION_V1_V2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // baseStyleAnchors and overrideStyleAnchors are added to DocumentEntity
        database.execSQL("ALTER TABLE DocumentEntity ADD COLUMN baseStyleAnchors TEXT NOT NULL DEFAULT '[]'")
        database.execSQL("ALTER TABLE DocumentEntity ADD COLUMN overrideStyleAnchors TEXT NOT NULL DEFAULT '[]'")
    }
}
