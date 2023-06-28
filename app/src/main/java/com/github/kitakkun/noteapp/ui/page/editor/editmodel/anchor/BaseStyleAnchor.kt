package com.github.kitakkun.noteapp.ui.page.editor.editmodel.anchor

import com.github.kitakkun.noteapp.ui.page.editor.editmodel.style.BaseStyle
import kotlinx.serialization.Serializable

@Serializable
data class BaseStyleAnchor(
    val line: Int,
    val style: BaseStyle,
) {
    fun isValid(lineCount: Int) = line in 0 until lineCount
}
