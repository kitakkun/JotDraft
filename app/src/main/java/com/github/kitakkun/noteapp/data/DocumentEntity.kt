package com.github.kitakkun.noteapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DocumentEntity(
    @PrimaryKey val title: String,
    val content: String,
//    val baseFormatSpans: List<IntRange>,
//    val boldSpans: List<IntRange>,
//    val italicSpans: List<IntRange>,
)