package com.github.kitakkun.noteapp.ui.page.editor

import androidx.compose.ui.graphics.Color
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
    val showSelectColorDialog: Boolean = false,
    val showColorPickerDialog: Boolean = false,

    val availableColors: List<Color> = listOf(
        Color.Black,
        Color.Red, Color.Green, Color.Blue,
        Color.Yellow, Color.Magenta, Color.Cyan,
    ),
    val currentColor: Color = Color.Black,
)
