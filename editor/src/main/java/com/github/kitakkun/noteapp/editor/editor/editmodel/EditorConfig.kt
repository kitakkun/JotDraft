package com.github.kitakkun.noteapp.editor.editor.editmodel

import com.github.kitakkun.noteapp.data.model.BaseStyle
import com.github.kitakkun.noteapp.data.model.StyleColor

data class EditorConfig(
    val baseStyle: BaseStyle = BaseStyle.Body,
    val isBold: Boolean = false,
    val isItalic: Boolean = false,
    val color: StyleColor = StyleColor.Dynamic(
        lightValue = androidx.compose.ui.graphics.Color.Black,
        darkValue = androidx.compose.ui.graphics.Color.White,
    ),
)
