package com.github.kitakkun.noteapp.ui.page.editor.editmodel.style

import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit

interface OverrideDocumentTextStyle : AbstractDocumentTextStyle {
    data class Bold(
        val enabled: Boolean,
    ) : OverrideDocumentTextStyle {
        override val spanStyle = SpanStyle(
            fontWeight = if (enabled) FontWeight.Bold else null
        )
    }

    data class Italic(
        val enabled: Boolean,
    ) : OverrideDocumentTextStyle {
        override val spanStyle = SpanStyle(
            fontStyle = if (enabled) FontStyle.Italic else null
        )
    }

    data class FontSize(val fontSize: TextUnit) : OverrideDocumentTextStyle {
        override val spanStyle = SpanStyle(
            fontSize = fontSize
        )
    }

    data class Color(val color: androidx.compose.ui.graphics.Color) : OverrideDocumentTextStyle {
        override val spanStyle = SpanStyle(
            color = color
        )
    }
}
