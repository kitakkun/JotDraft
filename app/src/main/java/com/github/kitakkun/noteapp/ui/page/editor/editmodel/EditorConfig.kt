package com.github.kitakkun.noteapp.ui.page.editor.editmodel

import com.github.kitakkun.noteapp.ui.page.editor.editmodel.style.BaseDocumentTextStyle

data class EditorConfig(
    val baseStyle: BaseDocumentTextStyle = BaseDocumentTextStyle.Body,
    val isBold: Boolean = false,
    val isItalic: Boolean = false,
)
