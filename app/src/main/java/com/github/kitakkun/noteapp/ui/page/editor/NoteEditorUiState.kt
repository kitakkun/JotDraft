package com.github.kitakkun.noteapp.ui.page.editor

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import com.github.kitakkun.noteapp.ui.page.editor.editmodel.EditorConfig
import com.github.kitakkun.noteapp.ui.page.editor.editmodel.anchor.StyleAnchor

data class NoteEditorUiState(
    // document raw data
    val documentTitle: String = "Untitled",
    val content: TextFieldValue = TextFieldValue(),

    // editor config
    val editorConfig: EditorConfig = EditorConfig(),

    // styles
    val styleAnchors: List<StyleAnchor> = emptyList(),

    // dialogs
    val showSelectBaseDocumentTextStyleDialog: Boolean = false,
    val showSelectColorDialog: Boolean = false,
    val showColorPickerDialog: Boolean = false,

    // colors
    val availableColors: List<Color> = listOf(
        Color.Black,
        Color.Red, Color.Green, Color.Blue,
        Color.Yellow, Color.Magenta, Color.Cyan,
    ),
    val currentColor: Color = Color.Black,

    // cursor あとでViewModelStateに移動したい
    val cursorStart: Int = 0,
    val cursorEnd: Int = 0,
)
