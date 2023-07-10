package com.github.kitakkun.noteapp.editor

import com.github.kitakkun.noteapp.data.model.OverrideStyle
import com.github.kitakkun.noteapp.data.model.OverrideStyleAnchor
import com.github.kitakkun.noteapp.data.model.StyleColor
import com.github.kitakkun.noteapp.editor.editmodel.EditorConfig
import com.github.kitakkun.noteapp.editor.usecase.AnchorTransformer
import org.junit.Assert.assertEquals
import org.junit.Test

class AnchorTransformerTest {
    @Test
    fun testAnchorGeneration() {
        val transformer = AnchorTransformer()
        val actual = transformer.generateAnchorsToInsert(
            editorConfig = EditorConfig(
                isBold = true,
                isItalic = true,
            ),
            insertPos = 0,
            length = 10,
        )
        val expected = listOf(
            OverrideStyleAnchor(
                start = 0,
                end = 10,
                style = OverrideStyle.Bold(enabled = true),
            ),
            OverrideStyleAnchor(
                start = 0,
                end = 10,
                style = OverrideStyle.Italic(enabled = true),
            ),
            OverrideStyleAnchor(
                start = 0,
                end = 10,
                style = OverrideStyle.Color(color = StyleColor.Dynamic.Default)
            )
        )
        assertEquals(expected.toSet(), actual.toSet())
    }
}
