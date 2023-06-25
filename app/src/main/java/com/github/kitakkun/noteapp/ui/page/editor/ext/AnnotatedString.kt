package com.github.kitakkun.noteapp.ui.page.editor.ext

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import com.github.kitakkun.noteapp.ui.page.editor.editmodel.anchor.BaseStyleAnchor
import com.github.kitakkun.noteapp.ui.page.editor.editmodel.anchor.OverrideStyleAnchor

fun AnnotatedString.applyStyles(
    baseStyleAnchors: List<BaseStyleAnchor>,
    overrideStyleAnchors: List<OverrideStyleAnchor>,
) = buildAnnotatedString {
    append(text = text)
    baseStyleAnchors.forEach {
        // +1 for the line break
        val start = text.lines().take(it.line).sumOf { line -> line.length + 1 }
        val end = start + text.lines()[it.line].length
        addStyle(
            style = it.style.spanStyle,
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
