package com.github.kitakkun.noteapp.ui.page.editor.editmodel.anchor

import com.github.kitakkun.noteapp.ui.page.editor.editmodel.style.AbstractDocumentTextStyle

data class StyleAnchor(
    val start: Int,
    val end: Int,
    val style: AbstractDocumentTextStyle,
) {
    fun shiftEnd(shift: Int) = copy(end = end + shift)
    fun shiftStart(shift: Int) = copy(start = start + shift)
    val isValid get() = start <= end
}
