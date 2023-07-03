package com.github.kitakkun.noteapp.editor.editor.composable

import android.graphics.Paint
import android.util.Range
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun EditorTextField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    visualTransformation: VisualTransformation,
    modifier: Modifier = Modifier,
) {
    val density = LocalDensity.current
    val scrollState = rememberScrollState()
    val scrollOffsetDp = remember {
        derivedStateOf {
            with(density) {
                scrollState.value.toDp()
            }
        }
    }

    val lineNumberDrawOffsetRanges = remember { mutableStateOf(listOf<Range<Float>>()) }
    val lineNumberFontSize = with(density) { 20.sp.toPx() }
    val lineNumberColor = MaterialTheme.colorScheme.onSurface
    val lineNumberPaint = remember {
        Paint().apply {
            color = lineNumberColor.toArgb()
            textSize = lineNumberFontSize
        }
    }

    Row(modifier = modifier) {
        Canvas(
            modifier = Modifier
                .width(30.dp)
                .offset(y = -scrollOffsetDp.value)
                .fillMaxHeight(),
            onDraw = {
                drawIntoCanvas { canvas ->
                    lineNumberDrawOffsetRanges.value.forEachIndexed { index, it ->
                        canvas.nativeCanvas.drawText(
                            index.toString(), 0f, (it.lower + it.upper + lineNumberFontSize) / 2f, lineNumberPaint
                        )
                    }
                }
            }
        )
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxHeight()
                .padding(horizontal = 8.dp)
                .verticalScroll(scrollState)
                .weight(1f),
            visualTransformation = visualTransformation,
            onTextLayout = { result ->
                // lineCount is increment by 1 when line is wrapped.
                // but when inserted a single linebreak, lineCount will be incremented by 2.
                val lineBreakOffsets = listOf(0) + value.text.mapIndexedNotNull { index, c ->
                    if (c == '\n') index + 1 else null
                }
                val lineBreakLines = lineBreakOffsets.map { result.getLineForOffset(it) }
                lineNumberDrawOffsetRanges.value = lineBreakLines.map {
                    Range(
                        result.multiParagraph.getLineTop(it),
                        result.multiParagraph.getLineBottom(it),
                    )
                }
            }
        )
    }
}
