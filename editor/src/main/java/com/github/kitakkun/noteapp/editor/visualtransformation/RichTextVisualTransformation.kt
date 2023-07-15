package com.github.kitakkun.noteapp.editor.visualtransformation

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import com.github.kitakkun.noteapp.data.model.BaseStyleAnchor
import com.github.kitakkun.noteapp.data.model.OverrideStyle
import com.github.kitakkun.noteapp.data.model.OverrideStyleAnchor
import com.github.kitakkun.noteapp.data.model.StyleColor

class RichTextVisualTransformation(
    private val baseStyleAnchors: List<BaseStyleAnchor>,
    private val overrideStyleAnchors: List<OverrideStyleAnchor>,
    private val isDarkTheme: Boolean,
) : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        return TransformedText(
            text = text.applyStyles(
                baseStyleAnchors = baseStyleAnchors,
                overrideStyleAnchors = overrideStyleAnchors,
                isDarkTheme = isDarkTheme,
            ),
            offsetMapping = OffsetMapping.Identity,
        )
    }
}

private fun AnnotatedString.applyStyles(
    baseStyleAnchors: List<BaseStyleAnchor>,
    overrideStyleAnchors: List<OverrideStyleAnchor>,
    isDarkTheme: Boolean,
) = buildAnnotatedString {
    append(text = text)
    /**
     * need to be sorted by line because the order of the style is important
     * paragraph styles are applied in a for loop which is in inner logic
     * see [AnnotatedString.normalizedParagraphStyles]
     * so if the style is applied in the wrong order,
     * it will result in a crash
     */
    baseStyleAnchors.sortedBy { it.line }.forEach {
        val isLastLine = it.line == text.lines().size - 1
        // +1 for the line break
        val start = text.lines().take(it.line).sumOf { line -> line.length + 1 }
        // if it is not the last line, add 1 for the line break
        val end = if (isLastLine) {
            start + text.lines()[it.line].length
        } else {
            start + text.lines()[it.line].length + 1
        }
        addStyle(
            style = it.style.spanStyle,
            start = start,
            end = end,
        )
        addStyle(
            style = it.style.paragraphStyle,
            start = start,
            end = end,
        )
    }
    overrideStyleAnchors.forEach { anchor ->
        if (anchor.style !is OverrideStyle.Color) {
            addStyle(
                style = anchor.style.spanStyle,
                start = anchor.start,
                end = anchor.end
            )
            return@forEach
        }
        val style = anchor.style as OverrideStyle.Color
        when (style.color) {
            is StyleColor.Dynamic -> {
                addStyle(
                    style = if (isDarkTheme) {
                        style.darkThemeSpanStyle
                    } else {
                        style.spanStyle
                    },
                    start = anchor.start,
                    end = anchor.end
                )
            }

            is StyleColor.Static -> {
                addStyle(
                    style = style.spanStyle,
                    start = anchor.start,
                    end = anchor.end
                )
            }
        }
    }
}
