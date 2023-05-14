package com.github.kitakkun.noteapp.ui.page.edit

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.kitakkun.noteapp.ui.page.edit.composable.EditorTopBar
import com.github.kitakkun.noteapp.ui.page.edit.composable.TextStyleConfig
import com.github.kitakkun.noteapp.ui.page.edit.composable.TextStyleControlRow
import com.github.kitakkun.noteapp.ui.preview.PreviewContainer

private fun buildStyledText(text: AnnotatedString) = buildAnnotatedString {
}

// just for reference
fun buildAnnotatedStringWithColors(text: String): AnnotatedString {
    val words: List<String> = text.split("\\s+".toRegex())// splits by whitespace
    val colors = listOf(Color.Red, Color.Black, Color.Yellow, Color.Blue)
    var count = 0

    val builder = AnnotatedString.Builder()
    for (word in words) {
        builder.withStyle(style = SpanStyle(color = colors[count % 4])) {
            append("$word ")
        }
        count++
    }
    return builder.toAnnotatedString()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteEditView(
    uiState: NoteEditUIState,
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
//           TODO: WIP
//            visualTransformation = {
//                TransformedText(
//                    buildAnnotatedStringWithColors(it.text),
//                    OffsetMapping.Identity,
//                )
//            }
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
private fun NoteEditViewPreview() = PreviewContainer {
    NoteEditView(
        uiState = NoteEditUIState(),
        onContentChange = {},
        onItalicChange = {},
        onBaseTextFormatClick = {},
        onBoldChange = {},
        onSaveClick = {},
        onNavigateUpClick = {},
    )
}
