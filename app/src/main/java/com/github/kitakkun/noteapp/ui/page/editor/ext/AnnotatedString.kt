package com.github.kitakkun.noteapp.ui.page.editor.ext

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import com.github.kitakkun.noteapp.ui.page.editor.editmodel.style.BaseDocumentTextStyle
import com.github.kitakkun.noteapp.ui.page.editor.editmodel.style.OverrideDocumentTextStyle

fun AnnotatedString.applyDocumentBaseTextStyle(
    baseFormats: List<AnnotatedString.Range<BaseDocumentTextStyle>>,
) = buildAnnotatedString {
    append(text = text)
    spanStyles.forEach {
        addStyle(it.item, it.start, it.end)
    }
    baseFormats.forEach {
        addStyle(it.item.spanStyle, it.start, it.end)
    }
}

fun AnnotatedString.applyOverrideTextStyle(
    overrideFormats: List<AnnotatedString.Range<OverrideDocumentTextStyle>>,
) = buildAnnotatedString {
    append(text = text)
    spanStyles.forEach {
        addStyle(it.item, it.start, it.end)
    }
    overrideFormats.forEach {
        addStyle(it.item.spanStyle, it.start, it.end)
    }
}