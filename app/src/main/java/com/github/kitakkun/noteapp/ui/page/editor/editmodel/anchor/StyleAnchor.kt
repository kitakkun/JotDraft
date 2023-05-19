package com.github.kitakkun.noteapp.ui.page.editor.editmodel.anchor

import com.github.kitakkun.noteapp.ui.page.editor.editmodel.style.AbstractDocumentTextStyle

data class StyleAnchor(
    val start: Int,
    val end: Int,
    val style: AbstractDocumentTextStyle,
) {
    fun isValid(textLength: Int) = start < end && start <= textLength && end <= textLength
}
