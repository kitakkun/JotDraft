package com.github.kitakkun.noteapp.ui.page.editor.editmodel

import androidx.compose.ui.graphics.Color
import com.github.kitakkun.noteapp.ui.page.editor.editmodel.style.BaseDocumentTextStyle

data class EditorConfig(
    val baseStyle: BaseDocumentTextStyle = BaseDocumentTextStyle.Body,
    val isBold: Boolean = false,
    val isItalic: Boolean = false,
    val color: Color = Color.Unspecified,
)
