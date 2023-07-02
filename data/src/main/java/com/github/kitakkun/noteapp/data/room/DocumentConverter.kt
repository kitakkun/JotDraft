package com.github.kitakkun.noteapp.data.room

import androidx.room.TypeConverter
import com.github.kitakkun.noteapp.data.model.BaseStyleAnchor
import com.github.kitakkun.noteapp.data.model.OverrideStyleAnchor
import com.google.gson.Gson

class DocumentConverter {
    @TypeConverter
    fun fromBaseStyleAnchorList(value: List<BaseStyleAnchor>): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toBaseStyleAnchorList(value: String): List<BaseStyleAnchor> {
        return Gson().fromJson(value, Array<BaseStyleAnchor>::class.java).toList()
    }

    @TypeConverter
    fun fromOverrideStyleAnchorList(value: List<OverrideStyleAnchor>): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toOverrideStyleAnchorList(value: String): List<OverrideStyleAnchor> {
        return Gson().fromJson(value, Array<OverrideStyleAnchor>::class.java).toList()
    }
}
