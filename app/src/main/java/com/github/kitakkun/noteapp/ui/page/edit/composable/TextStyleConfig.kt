package com.github.kitakkun.noteapp.ui.page.edit.composable

import androidx.compose.ui.graphics.Color
import com.github.kitakkun.noteapp.ui.page.edit.editmodel.BaseTextFormat

data class TextStyleConfig(
    val baseFormat: BaseTextFormat = BaseTextFormat.Body,
    val bold: Boolean = false,
    val italic: Boolean = false,
    val color: Color = Color.Unspecified,
)
