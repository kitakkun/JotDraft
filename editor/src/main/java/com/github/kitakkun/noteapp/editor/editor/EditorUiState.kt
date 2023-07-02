package com.github.kitakkun.noteapp.editor.editor

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import com.github.kitakkun.noteapp.data.model.BaseStyleAnchor
import com.github.kitakkun.noteapp.data.model.OverrideStyleAnchor
import com.github.kitakkun.noteapp.editor.editor.editmodel.EditorConfig

data class EditorUiState(
    // document raw data
    val documentTitle: String = "Untitled",
    val content: TextFieldValue = TextFieldValue(),

    // editor config
    val editorConfig: EditorConfig = EditorConfig(),

    // styles
    val baseStyleAnchors: List<BaseStyleAnchor> = emptyList(),
    val overrideStyleAnchors: List<OverrideStyleAnchor> = emptyList(),

    // dialogs
    val showSelectBaseStyleDialog: Boolean = false,
    val showSelectColorDialog: Boolean = false,
    val showColorPickerDialog: Boolean = false,

    // colors
    val availableColors: List<Color> = listOf(
        Color.Black,
        Color.Red, Color.Green, Color.Blue,
        Color.Yellow, Color.Magenta, Color.Cyan,
    ),

    val canUndo: Boolean = false,
    val canRedo: Boolean = false,

    val documentId: String = "",
    val documentExists: Boolean = false,

    val isSavingDocument: Boolean = false,
)
