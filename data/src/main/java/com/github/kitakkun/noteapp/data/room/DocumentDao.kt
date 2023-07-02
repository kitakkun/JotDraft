package com.github.kitakkun.noteapp.data.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface DocumentDao {
    @Query("SELECT * FROM DocumentEntity")
    fun getAll(): List<DocumentEntity>

    @Query("SELECT * FROM DocumentEntity WHERE title LIKE :title")
    fun findByTitle(title: String): DocumentEntity

    @Insert
    fun insertAll(vararg documents: DocumentEntity)

    @Insert
    fun insert(document: DocumentEntity)

    @Update
    fun update(document: DocumentEntity)

    @Delete
    fun delete(document: DocumentEntity)

    @Query("DELETE FROM DocumentEntity WHERE id = :documentId")
    fun deleteById(documentId: String?)

    @Query("SELECT * FROM DocumentEntity WHERE id = :documentId")
    fun getById(documentId: String?): DocumentEntity?
}