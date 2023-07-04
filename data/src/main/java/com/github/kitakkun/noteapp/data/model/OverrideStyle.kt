package com.github.kitakkun.noteapp.data.model

import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
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

    data class Color(
        val color: StyleColor,
    ) : OverrideStyle {
        override val spanStyle = SpanStyle(
            color = when (color) {
                is StyleColor.Dynamic -> color.lightValue
                is StyleColor.Static -> color.value
            }
        )
        val darkThemeSpanStyle = SpanStyle(
            color = when (color) {
                is StyleColor.Dynamic -> color.darkValue
                is StyleColor.Static -> color.value
            }
        )
    }
}
