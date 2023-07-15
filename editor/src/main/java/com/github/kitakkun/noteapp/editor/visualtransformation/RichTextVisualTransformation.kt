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
    val lines = text.lines()
    val lineCount = text.lines().size
    baseStyleAnchors.sortedBy { it.line }.forEach { anchor ->
        val lineContent = lines[anchor.line]
        val leadingLines = lines.take(anchor.line)
        // +1 for the line break
        val startOffset = leadingLines.sumOf { line -> line.length + 1 }
        // if it is not the last line, add 1 for the line break
        val endOffset = when (anchor.line == lineCount - 1) {
            true -> startOffset + lineContent.length
            false -> startOffset + lineContent.length + 1
        }
        addStyle(
            style = anchor.style.spanStyle,
            start = startOffset,
            end = endOffset,
        )
        addStyle(
            style = anchor.style.paragraphStyle,
            start = startOffset,
            end = endOffset,
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
                    style = when (isDarkTheme) {
                        true -> style.darkThemeSpanStyle
                        false -> style.spanStyle
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
