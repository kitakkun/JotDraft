package com.github.kitakkun.noteapp.editor.ext

import androidx.compose.ui.text.TextRange

fun TextRange.toValidOrder(): TextRange = when {
    reversed -> TextRange(end, start)
    else -> this
}
