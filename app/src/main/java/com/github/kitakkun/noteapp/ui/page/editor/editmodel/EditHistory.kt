package com.github.kitakkun.noteapp.ui.page.editor.editmodel

import androidx.compose.ui.text.input.TextFieldValue
import com.github.kitakkun.noteapp.data.model.BaseStyleAnchor
import com.github.kitakkun.noteapp.data.model.OverrideStyleAnchor

data class EditHistory(
    val content: TextFieldValue,
    val baseStyleAnchors: List<BaseStyleAnchor>,
    val overrideStyleAnchors: List<OverrideStyleAnchor>,
)
