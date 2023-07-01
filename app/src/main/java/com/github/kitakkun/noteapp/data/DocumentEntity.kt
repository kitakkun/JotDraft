package com.github.kitakkun.noteapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.github.kitakkun.noteapp.ui.page.editor.editmodel.anchor.BaseStyleAnchor
import com.github.kitakkun.noteapp.ui.page.editor.editmodel.anchor.OverrideStyleAnchor
import java.util.UUID

@Entity
data class DocumentEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val title: String,
    val content: String,
    val baseStyleAnchors: List<BaseStyleAnchor>,
    val overrideStyleAnchors: List<OverrideStyleAnchor>,
)
