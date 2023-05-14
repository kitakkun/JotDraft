package com.github.kitakkun.noteapp.ui.page.editor

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.kitakkun.noteapp.ui.page.editor.composable.EditorTopBar
import com.github.kitakkun.noteapp.ui.page.editor.composable.TextStyleConfig
import com.github.kitakkun.noteapp.ui.page.editor.composable.TextStyleControlRow
import com.github.kitakkun.noteapp.ui.page.editor.ext.applyDocumentBaseTextStyle
import com.github.kitakkun.noteapp.ui.page.editor.ext.applyOverrideTextStyle
import com.github.kitakkun.noteapp.ui.preview.PreviewContainer

private fun buildStyledText(text: AnnotatedString) = buildAnnotatedString {
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteEditorView(
    uiState: NoteEditorUiState,
    onContentChange: (String) -> Unit,
    onBoldChange: (Boolean) -> Unit,
    onItalicChange: (Boolean) -> Unit,
    onBaseTextFormatClick: () -> Unit,
    onNavigateUpClick: () -> Unit,
    onSaveClick: () -> Unit,
) {
    Scaffold(
        topBar = {
            EditorTopBar(
                title = uiState.documentTitle,
                onNavigateBeforeClick = onNavigateUpClick,
                onSaveClick = onSaveClick,
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            BasicTextField(
                value = uiState.rawContent,
                onValueChange = onContentChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
                    .weight(1f),
                visualTransformation = {
                    TransformedText(
                        text = it.applyDocumentBaseTextStyle(baseFormats = uiState.baseStyles)
                            .applyOverrideTextStyle(overrideFormats = uiState.overrideStyles),
                        offsetMapping = OffsetMapping.Identity,
                    )
                }
            )
            TextStyleControlRow(
                config = TextStyleConfig(),
                onBoldChange = onBoldChange,
                onItalicChange = onItalicChange,
                onBaseTextFormatClick = onBaseTextFormatClick,
            )
        }

    }
}

@Preview
@Composable
private fun NoteEditorViewPreview() = PreviewContainer {
    NoteEditorView(
        uiState = NoteEditorUiState(),
        onContentChange = {},
        onItalicChange = {},
        onBaseTextFormatClick = {},
        onBoldChange = {},
        onSaveClick = {},
        onNavigateUpClick = {},
    )
}
