package com.github.kitakkun.noteapp.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.github.kitakkun.noteapp.data.model.BaseStyleAnchor
import com.github.kitakkun.noteapp.data.model.OverrideStyleAnchor
import java.util.UUID

@Entity
data class DocumentEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val title: String,
    val content: String,
    val baseStyleAnchors: List<BaseStyleAnchor>,
    val overrideStyleAnchors: List<OverrideStyleAnchor>,
)
