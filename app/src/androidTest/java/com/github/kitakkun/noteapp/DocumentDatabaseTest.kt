package com.github.kitakkun.noteapp

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.kitakkun.noteapp.data.DocumentDao
import com.github.kitakkun.noteapp.data.DocumentDatabase
import com.github.kitakkun.noteapp.data.DocumentEntity
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class DocumentDatabaseTest {
    private lateinit var database: DocumentDatabase
    private lateinit var documentDao: DocumentDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(
            context = context,
            klass = DocumentDatabase::class.java,
        ).build()
        documentDao = database.documentDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDatabase() {
        database.close()
    }

    @Test
    fun testInsert() {
        documentDao.insertAll(
            DocumentEntity(
                "title",
                "content",
            )
        )
        assertEquals(1, documentDao.getAll().size)
    }

    @Test
    fun testMultipleInsert() {
        documentDao.insertAll(
            DocumentEntity(
                "title",
                "content",
            ),
            DocumentEntity(
                "title2",
                "content2",
            ),
        )
        assertEquals(2, documentDao.getAll().size)
    }

    @Test
    fun testDelete() {
        val document = DocumentEntity(
            "title",
            "content",
        )
        documentDao.insertAll(document)
        assertEquals(1, documentDao.getAll().size)
        documentDao.delete(document)
        assertEquals(0, documentDao.getAll().size)
    }
}