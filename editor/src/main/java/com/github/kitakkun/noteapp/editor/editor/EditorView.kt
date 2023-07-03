package com.github.kitakkun.noteapp.editor.editor

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.kitakkun.noteapp.customview.preview.PreviewContainer
import com.github.kitakkun.noteapp.data.model.BaseStyle
import com.github.kitakkun.noteapp.editor.editor.composable.EditorTopBar
import com.github.kitakkun.noteapp.editor.editor.composable.TextStyleControlRow
import com.github.kitakkun.noteapp.editor.editor.dialog.colorpicker.ColorPickerDialog
import com.github.kitakkun.noteapp.editor.editor.dialog.colorselect.SelectColorDialog
import com.github.kitakkun.noteapp.editor.editor.dialog.selectbasedocument.SelectBaseStyleDialog
import com.github.kitakkun.noteapp.editor.editor.ext.applyStyles

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
            val pairs = remember { mutableStateOf(listOf<Pair<Float, Float>>()) }
            val scrollState = rememberScrollState()
            Row(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp)
            ) {
                val density = LocalDensity.current
                Canvas(
                    modifier = Modifier
                        .width(30.dp)
                        .offset(y = with(density) { -scrollState.value.toDp() })
                        .fillMaxHeight(),
                    onDraw = {
                        drawIntoCanvas { canvas ->
                            pairs.value.forEachIndexed { index, it ->
                                canvas.nativeCanvas.drawText(
                                    index.toString(),
                                    0f,
                                    (it.first + it.second + 24.sp.toPx()) / 2f,
                                    android.graphics.Paint().apply {
                                        color = android.graphics.Color.RED
                                        textSize = 24.sp.toPx()
                                    }
                                )
                            }
                        }
                    }
                )
                BasicTextField(
                    value = uiState.content,
                    onValueChange = onContentChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .padding(horizontal = 8.dp)
                        .verticalScroll(scrollState)
                        .weight(1f),
                    visualTransformation = {
                        TransformedText(
                            text = it.applyStyles(
                                baseStyleAnchors = uiState.baseStyleAnchors,
                                overrideStyleAnchors = uiState.overrideStyleAnchors
                            ),
                            offsetMapping = OffsetMapping.Identity,
                        )
                    },
                    onTextLayout = { result ->
                        // lineCount is increment by 1 when line is wrapped.
                        // but when inserted a single linebreak, lineCount will be incremented by 2.
                        val lineBreakOffsets = uiState.content.text.mapIndexedNotNull { index, c ->
                            if (c == '\n') index else null
                        }
                        val lineBreakLines = lineBreakOffsets.map { result.getLineForOffset(it) }
                        pairs.value = lineBreakLines.map {
                            result.multiParagraph.getLineTop(it) to result.multiParagraph.getLineBottom(it)
                        }
                    }
                )
            }
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
