package com.github.kitakkun.noteapp.ui.page.editor.editmodel.style

import androidx.compose.ui.unit.TextUnit

interface OverrideDocumentTextStyle : AbstractDocumentTextStyle {
    object Bold : OverrideDocumentTextStyle
    object Italic : OverrideDocumentTextStyle
    data class FontSize(val fontSize: TextUnit) : OverrideDocumentTextStyle
    data class Color(val color: androidx.compose.ui.graphics.Color) : OverrideDocumentTextStyle
}