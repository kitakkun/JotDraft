package com.github.kitakkun.noteapp.editor.editor.ext

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb

fun Color.toHexCode(): String {
    return "#%X".format(this.toArgb())
}
