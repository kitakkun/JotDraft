package com.github.kitakkun.noteapp.ui.page.editor.editmodel

import com.github.kitakkun.noteapp.ui.page.editor.editmodel.style.BaseDocumentTextStyle

data class EditorConfig(
    val baseStyle: BaseDocumentTextStyle,
    val isBold: Boolean,
    val isItalic: Boolean,
)
