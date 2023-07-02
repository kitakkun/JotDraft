package com.github.kitakkun.noteapp.data.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface DocumentDao {
    @Query("SELECT * FROM DocumentEntity")
    suspend fun getAll(): List<DocumentEntity>

    @Query("SELECT * FROM DocumentEntity WHERE title LIKE :title")
    suspend fun findByTitle(title: String): DocumentEntity

    @Insert
    suspend fun insertAll(vararg documents: DocumentEntity)

    @Insert
    suspend fun insert(document: DocumentEntity)

    @Update
    suspend fun update(document: DocumentEntity)

    @Delete
    suspend fun delete(document: DocumentEntity)

    @Query("DELETE FROM DocumentEntity WHERE id = :documentId")
    suspend fun deleteById(documentId: String?)

    @Query("SELECT * FROM DocumentEntity WHERE id = :documentId")
    suspend fun getById(documentId: String?): DocumentEntity?
}
