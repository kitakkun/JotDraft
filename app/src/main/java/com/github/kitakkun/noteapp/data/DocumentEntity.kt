package com.github.kitakkun.noteapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity
data class DocumentEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val title: String,
    val content: String,
//    val baseFormatSpans: List<IntRange>,
//    val boldSpans: List<IntRange>,
//    val italicSpans: List<IntRange>,
)
