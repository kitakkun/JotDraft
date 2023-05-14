package com.github.kitakkun.noteapp.ui.page.editor.composable

import androidx.compose.ui.graphics.Color
import com.github.kitakkun.noteapp.ui.page.editor.editmodel.style.BaseDocumentTextStyle

data class TextStyleConfig(
    val baseStyle: BaseDocumentTextStyle = BaseDocumentTextStyle.Body,
    val bold: Boolean = false,
    val italic: Boolean = false,
    val color: Color = Color.Unspecified,
)
