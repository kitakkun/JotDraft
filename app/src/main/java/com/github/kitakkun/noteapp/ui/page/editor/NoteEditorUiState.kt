package com.github.kitakkun.noteapp.ui.page.editor

import androidx.compose.ui.text.input.TextFieldValue
import com.github.kitakkun.noteapp.ui.page.editor.editmodel.EditorConfig
import com.github.kitakkun.noteapp.ui.page.editor.editmodel.anchor.StyleAnchor

data class NoteEditorUiState(
    val documentTitle: String = "Untitled",
    val content: TextFieldValue = TextFieldValue(),

    val editorConfig: EditorConfig = EditorConfig(),

    val baseStyleAnchors: List<StyleAnchor> = emptyList(),
    val overrideStyles: List<StyleAnchor> = emptyList(),

    val showSelectBaseDocumentTextStyleDialog: Boolean = false,
)
