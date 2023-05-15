package com.github.kitakkun.noteapp.ui.page.editor.ext

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import com.github.kitakkun.noteapp.ui.page.editor.editmodel.anchor.StyleAnchor

fun AnnotatedString.applyStyles(styleAnchors: List<StyleAnchor>) = buildAnnotatedString {
    append(text = text)
    spanStyles.forEach {
        addStyle(it.item, it.start, it.end)
    }
    styleAnchors.forEach {
        addStyle(
            style = it.style.spanStyle,
            start = it.start,
            end = it.end ?: text.length
        )
    }
}

fun AnnotatedString.applyStyle(styleAnchor: StyleAnchor) = buildAnnotatedString {
    append(text = text)
    spanStyles.forEach {
        addStyle(it.item, it.start, it.end)
    }
    addStyle(
        style = styleAnchor.style.spanStyle,
        start = styleAnchor.start,
        end = styleAnchor.end ?: text.length
    )
}
