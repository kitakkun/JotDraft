package com.github.kitakkun.noteapp.ui.page.editor.editmodel.anchor

import com.github.kitakkun.noteapp.ui.page.editor.editmodel.style.OverrideStyle

data class OverrideStyleAnchor(
    val start: Int,
    val end: Int,
    val style: OverrideStyle,
) {
    fun isValid(textLength: Int) = start < end && start <= textLength && end <= textLength
}
