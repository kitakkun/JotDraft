package com.github.kitakkun.noteapp.data

import androidx.room.TypeConverter

class DocumentConverter {
    @TypeConverter
    fun fromIntRangeList(value: List<IntRange>): String {
        if (value.isEmpty()) {
            return ""
        }
        return value.joinToString(separator = ",") { "${it.first},${it.last}" }
    }

    @TypeConverter
    fun toIntRangeList(value: String): List<IntRange> {
        if (value.isEmpty()) {
            return emptyList()
        }
        return value.split(",").map {
            IntRange(it.split(",")[0].toInt(), it.split(",")[1].toInt())
        }
    }
}