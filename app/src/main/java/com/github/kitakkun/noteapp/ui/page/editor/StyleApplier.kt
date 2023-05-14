package com.github.kitakkun.noteapp.ui.page.editor

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import com.github.kitakkun.noteapp.ui.page.editor.editmodel.BaseTextFormat

class StyleApplier : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        TODO("Not yet implemented")
    }

    private fun applyBaseFormat(
        annotatedString: AnnotatedString,
        baseFormats: List<AnnotatedString.Range<BaseTextFormat>>,
    ) = buildAnnotatedString {
        append(text = annotatedString.text)
        annotatedString.spanStyles.forEach {
            addStyle(it.item, it.start, it.end)
        }
        baseFormats.forEach {
            addStyle(it.item.spanStyle, it.start, it.end)
        }
    }
}