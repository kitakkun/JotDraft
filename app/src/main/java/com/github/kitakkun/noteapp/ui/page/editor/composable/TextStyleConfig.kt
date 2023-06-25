package com.github.kitakkun.noteapp.ui.page.editor.composable

import androidx.compose.ui.graphics.Color
import com.github.kitakkun.noteapp.ui.page.editor.editmodel.style.BaseStyle

data class TextStyleConfig(
    val baseStyle: BaseStyle = BaseStyle.Body,
    val bold: Boolean = false,
    val italic: Boolean = false,
    val color: Color = Color.Unspecified,
)
