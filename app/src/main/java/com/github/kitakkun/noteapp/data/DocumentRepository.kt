package com.github.kitakkun.noteapp.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DocumentRepository(
    private val documentDao: DocumentDao
) {
    suspend fun saveDocument(
        title: String,
        rawContent: String,
    ) = withContext(Dispatchers.IO) {
        documentDao.insert(
            DocumentEntity(
                title = title,
                content = rawContent,
            )
        )
    }

    suspend fun updateDocument(
        title: String,
        rawContent: String,
    ) = withContext(Dispatchers.IO) {
        documentDao.update(
            DocumentEntity(
                title = title,
                content = rawContent,
            )
        )
    }

    suspend fun deleteDocument(
        title: String,
    ) = withContext(Dispatchers.IO) {
    }

    suspend fun fetchDocuments() = withContext(Dispatchers.IO) {
        documentDao.getAll()
    }
}
