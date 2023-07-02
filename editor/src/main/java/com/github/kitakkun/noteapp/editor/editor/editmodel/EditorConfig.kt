package com.github.kitakkun.noteapp.editor.editor.editmodel

import androidx.compose.ui.graphics.Color
import com.github.kitakkun.noteapp.data.model.BaseStyle

data class EditorConfig(
    val baseStyle: BaseStyle = BaseStyle.Body,
    val isBold: Boolean = false,
    val isItalic: Boolean = false,
    val color: Color = Color.Unspecified,
)
