package com.github.kitakkun.noteapp.ui.page.editor

import androidx.compose.ui.text.AnnotatedString
import com.github.kitakkun.noteapp.ui.page.editor.editmodel.EditorConfig
import com.github.kitakkun.noteapp.ui.page.editor.editmodel.style.BaseDocumentTextStyle
import com.github.kitakkun.noteapp.ui.page.editor.editmodel.style.OverrideDocumentTextStyle

data class NoteEditorUiState(
    val documentTitle: String = "Untitled",
    val rawContent: String = "",

    val editorConfig: EditorConfig = EditorConfig(),

    val baseStyles: List<AnnotatedString.Range<BaseDocumentTextStyle>> = emptyList(),
    val overrideStyles: List<AnnotatedString.Range<OverrideDocumentTextStyle>> = emptyList(),
)