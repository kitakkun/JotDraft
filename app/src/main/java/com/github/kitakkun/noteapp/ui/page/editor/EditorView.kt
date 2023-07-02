package com.github.kitakkun.noteapp.ui.page.editor

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.kitakkun.noteapp.customview.preview.PreviewContainer
import com.github.kitakkun.noteapp.data.model.BaseStyle
import com.github.kitakkun.noteapp.ui.page.editor.composable.EditorTopBar
import com.github.kitakkun.noteapp.ui.page.editor.composable.TextStyleControlRow
import com.github.kitakkun.noteapp.ui.page.editor.dialog.colorpicker.ColorPickerDialog
import com.github.kitakkun.noteapp.ui.page.editor.dialog.colorselect.SelectColorDialog
import com.github.kitakkun.noteapp.ui.page.editor.dialog.selectbasedocument.SelectBaseStyleDialog
import com.github.kitakkun.noteapp.ui.page.editor.ext.applyStyles

@OptIn(ExperimentalMaterial3Api::class)
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
    onAddColorFinished: (Color) -> Unit,
    onColorPickerOpenRequest: () -> Unit,
    onColorSelected: (Color) -> Unit,
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

    Scaffold(
        topBar = {
            EditorTopBar(
                title = uiState.documentTitle,
                onNavigateBeforeClick = onNavigateUpClick,
                onSaveClick = onSaveClick,
                onTitleChange = onTitleChange,
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            BasicTextField(
                value = uiState.content,
                onValueChange = onContentChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
                    .weight(1f),
                visualTransformation = {
                    TransformedText(
                        text = it.applyStyles(
                            baseStyleAnchors = uiState.baseStyleAnchors,
                            overrideStyleAnchors = uiState.overrideStyleAnchors
                        ),
                        offsetMapping = OffsetMapping.Identity,
                    )
                }
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
    )
}
