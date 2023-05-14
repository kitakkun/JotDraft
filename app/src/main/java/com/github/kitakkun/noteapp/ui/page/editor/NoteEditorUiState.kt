package com.github.kitakkun.noteapp.ui.page.editor

import androidx.compose.ui.text.AnnotatedString
import com.github.kitakkun.noteapp.ui.page.editor.editmodel.BaseTextFormat

data class NoteEditorUiState(
    val documentTitle: String = "Untitled",
    val isBold: Boolean = false,
    val isItalic: Boolean = false,
    val rawContent: String = "",
    val baseFormatSpans: List<AnnotatedString.Range<BaseTextFormat>> = emptyList(),
    val boldSpans: List<IntRange> = emptyList(),
    val italicSpans: List<IntRange> = emptyList(),
)