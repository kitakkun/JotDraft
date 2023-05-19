package com.github.kitakkun.noteapp.ui.page.editor.ext

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import com.github.kitakkun.noteapp.ui.page.editor.editmodel.anchor.StyleAnchor
import com.github.kitakkun.noteapp.ui.page.editor.editmodel.style.BaseDocumentTextStyle
import com.github.kitakkun.noteapp.ui.page.editor.editmodel.style.OverrideDocumentTextStyle

fun AnnotatedString.applyStyles(styleAnchors: List<StyleAnchor>) = buildAnnotatedString {
    append(text = text)
    styleAnchors.filter { it.style is BaseDocumentTextStyle }.forEach {
        addStyle(
            style = it.style.spanStyle,
            start = it.start,
            end = it.end
        )
    }
    styleAnchors.filter { it.style is OverrideDocumentTextStyle }.forEach {
        addStyle(
            style = it.style.spanStyle,
            start = it.start,
            end = it.end
        )
    }
}
