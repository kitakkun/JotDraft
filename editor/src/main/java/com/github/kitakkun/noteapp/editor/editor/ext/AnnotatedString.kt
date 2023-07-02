package com.github.kitakkun.noteapp.editor.editor.ext

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import com.github.kitakkun.noteapp.data.model.BaseStyleAnchor
import com.github.kitakkun.noteapp.data.model.OverrideStyleAnchor

fun AnnotatedString.applyStyles(
    baseStyleAnchors: List<BaseStyleAnchor>,
    overrideStyleAnchors: List<OverrideStyleAnchor>,
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
    overrideStyleAnchors.forEach {
        addStyle(
            style = it.style.spanStyle,
            start = it.start,
            end = it.end
        )
    }
}
