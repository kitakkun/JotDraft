package com.github.kitakkun.noteapp.editor.ext

import androidx.compose.ui.text.input.TextFieldValue

fun TextFieldValue.getLineAtStartCursor(): Int {
    val cursorPosition = this.selection.start
    if (cursorPosition == 0) return 0
    val textBeforeCursor = this.text.substring(0, cursorPosition)
    return textBeforeCursor.count { it == '\n' }
}

fun TextFieldValue.getLineAtEndCursor(): Int {
    val cursorPosition = this.selection.end
    if (cursorPosition == 0) return 0
    val textBeforeCursor = this.text.substring(0, cursorPosition)
    return textBeforeCursor.count { it == '\n' }
}

