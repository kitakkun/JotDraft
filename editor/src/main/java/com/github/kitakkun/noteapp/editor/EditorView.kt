package com.github.kitakkun.noteapp.editor

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.tooling.preview.Preview
import com.github.kitakkun.noteapp.customview.preview.PreviewContainer
import com.github.kitakkun.noteapp.data.model.BaseStyle
import com.github.kitakkun.noteapp.data.model.StyleColor
import com.github.kitakkun.noteapp.editor.composable.EditorTextField
import com.github.kitakkun.noteapp.editor.composable.EditorTopBar
import com.github.kitakkun.noteapp.editor.composable.TextStyleControlRow
import com.github.kitakkun.noteapp.editor.dialog.colorpicker.ColorPickerDialog
import com.github.kitakkun.noteapp.editor.dialog.colorselect.SelectColorDialog
import com.github.kitakkun.noteapp.editor.dialog.selectbasedocument.SelectBaseStyleDialog
import com.github.kitakkun.noteapp.editor.ext.applyStyles

@Composable
fun EditorView(
    uiState: EditorUiState,
    onContentChange: (TextFieldValue) -> Unit,
    onBoldChange: (Boolean) -> Unit,
    onItalicChange: (Boolean) -> Unit,
    onTitleChange: (String) -> Unit,
    onBaseTextFormatClick: () -> Unit,
    onNavigateUpClick: () -> Unit,
    onSaveClick: () -> Unit,
    onBaseStyleChange: (BaseStyle) -> Unit,
    onTextColorIconClick: () -> Unit,
    onDismissSelectBaseDocumentTextStyleDialog: () -> Unit,
    onDismissColorPickerDialog: () -> Unit,
    onDismissSelectColorDialog: () -> Unit,
    onAddColorFinished: (StyleColor) -> Unit,
    onColorPickerOpenRequest: () -> Unit,
    onColorSelected: (StyleColor) -> Unit,
    onClickRedo: () -> Unit,
    onClickUndo: () -> Unit,
) {
    if (uiState.showColorPickerDialog) {
        ColorPickerDialog(
            onDismiss = onDismissColorPickerDialog,
            onCancel = onDismissColorPickerDialog,
            onColorConfirm = onAddColorFinished,
        )
    }
    if (uiState.showSelectColorDialog) {
        SelectColorDialog(
            availableColors = uiState.availableColors,
            selectedColor = uiState.editorConfig.color,
            onDismiss = onDismissSelectColorDialog,
            onColorSelected = onColorSelected,
            onAddColorClick = onColorPickerOpenRequest,
        )
    }
    if (uiState.showSelectBaseStyleDialog) {
        SelectBaseStyleDialog(
            selectedStyle = uiState.editorConfig.baseStyle,
            onSelectStyle = onBaseStyleChange,
            onDismissRequest = onDismissSelectBaseDocumentTextStyleDialog,
        )
    }

    val isDarkTheme = isSystemInDarkTheme()

    Scaffold(
        topBar = {
            EditorTopBar(
                title = uiState.documentTitle,
                undoEnabled = uiState.canUndo,
                redoEnabled = uiState.canRedo,
                saveButtonEnabled = !uiState.isSavingDocument,
                onNavigateBeforeClick = onNavigateUpClick,
                onSaveClick = onSaveClick,
                onTitleChange = onTitleChange,
                onClickRedo = onClickRedo,
                onClickUndo = onClickUndo,
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            EditorTextField(
                value = uiState.content,
                showLineNumber = uiState.showLineNumber,
                onValueChange = onContentChange,
                visualTransformation = {
                    TransformedText(
                        text = it.applyStyles(
                            baseStyleAnchors = uiState.baseStyleAnchors,
                            overrideStyleAnchors = uiState.overrideStyleAnchors,
                            isDarkTheme = isDarkTheme,
                        ),
                        offsetMapping = OffsetMapping.Identity,
                    )
                },
                modifier = Modifier.weight(1f)
            )
            TextStyleControlRow(
                config = uiState.editorConfig,
                color = uiState.editorConfig.color,
                onBoldChange = onBoldChange,
                onItalicChange = onItalicChange,
                onBaseTextFormatClick = onBaseTextFormatClick,
                onTextColorIconClick = onTextColorIconClick,
            )
        }

    }
}

@Preview
@Composable
private fun NoteEditorViewPreview() = PreviewContainer {
    EditorView(
        uiState = EditorUiState(),
        onContentChange = {},
        onItalicChange = {},
        onBaseTextFormatClick = {},
        onBoldChange = {},
        onSaveClick = {},
        onNavigateUpClick = {},
        onBaseStyleChange = {},
        onDismissSelectBaseDocumentTextStyleDialog = {},
        onColorSelected = {},
        onDismissColorPickerDialog = {},
        onTextColorIconClick = {},
        onDismissSelectColorDialog = {},
        onAddColorFinished = {},
        onColorPickerOpenRequest = {},
        onTitleChange = {},
        onClickRedo = {},
        onClickUndo = {},
    )
}
