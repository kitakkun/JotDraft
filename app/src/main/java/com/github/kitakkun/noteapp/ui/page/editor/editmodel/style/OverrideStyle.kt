package com.github.kitakkun.noteapp.ui.page.editor.editmodel.style

import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import com.github.kitakkun.noteapp.ui.page.editor.editmodel.serializer.OverrideStyleTypeAdapter
import com.google.gson.annotations.JsonAdapter

@JsonAdapter(OverrideStyleTypeAdapter::class)
sealed interface OverrideStyle {
    val spanStyle: SpanStyle

    data class Bold(
        val enabled: Boolean,
    ) : OverrideStyle {
        override val spanStyle = SpanStyle(fontWeight = if (enabled) FontWeight.Bold else null)
    }

    data class Italic(
        val enabled: Boolean,
    ) : OverrideStyle {
        override val spanStyle = SpanStyle(fontStyle = if (enabled) FontStyle.Italic else null)
    }

    data class FontSize(val fontSize: TextUnit) : OverrideStyle {
        override val spanStyle = SpanStyle(fontSize = fontSize)
    }

    data class Color(val color: androidx.compose.ui.graphics.Color) : OverrideStyle {
        override val spanStyle = SpanStyle(color = color)
    }
}
