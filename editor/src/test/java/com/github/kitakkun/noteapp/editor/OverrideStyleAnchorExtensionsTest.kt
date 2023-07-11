package com.github.kitakkun.noteapp.editor

import com.github.kitakkun.noteapp.data.model.OverrideStyle
import com.github.kitakkun.noteapp.data.model.OverrideStyleAnchor
import com.github.kitakkun.noteapp.data.model.StyleColor
import com.github.kitakkun.noteapp.editor.ext.optimize
import com.github.kitakkun.noteapp.editor.ext.shiftToLeft
import com.github.kitakkun.noteapp.editor.ext.shiftToRight
import com.github.kitakkun.noteapp.editor.ext.sortToOptimize
import com.github.kitakkun.noteapp.editor.ext.splitAt
import org.junit.Assert.assertEquals
import org.junit.Test

class OverrideStyleAnchorExtensionsTest {
    @Test
    fun testSplit() {
        val anchors = listOf(
            OverrideStyleAnchor(start = 0, end = 10, style = OverrideStyle.Bold(enabled = true)),
            OverrideStyleAnchor(start = 0, end = 10, style = OverrideStyle.Italic(enabled = true)),
            OverrideStyleAnchor(start = 0, end = 10, style = OverrideStyle.Color(color = StyleColor.Dynamic.Default)),
        )
        val splitIndex = 4
        val actual = anchors.splitAt(splitIndex)
        val expected = listOf(
            OverrideStyleAnchor(start = 0, end = 4, style = OverrideStyle.Bold(enabled = true)),
            OverrideStyleAnchor(start = 4, end = 10, style = OverrideStyle.Bold(enabled = true)),
            OverrideStyleAnchor(start = 0, end = 4, style = OverrideStyle.Italic(enabled = true)),
            OverrideStyleAnchor(start = 4, end = 10, style = OverrideStyle.Italic(enabled = true)),
            OverrideStyleAnchor(start = 0, end = 4, style = OverrideStyle.Color(color = StyleColor.Dynamic.Default)),
            OverrideStyleAnchor(start = 4, end = 10, style = OverrideStyle.Color(color = StyleColor.Dynamic.Default)),
        )
        assertEquals(expected, actual)
    }

    @Test
    fun testShiftToLeft() {
        val anchors = listOf(
            OverrideStyleAnchor(start = 10, end = 18, style = OverrideStyle.Bold(enabled = true)),
            OverrideStyleAnchor(start = 15, end = 25, style = OverrideStyle.Italic(enabled = true)),
            OverrideStyleAnchor(start = 15, end = 20, style = OverrideStyle.Color(color = StyleColor.Dynamic.Default)),
        )
        val baseOffset = 11
        val shiftOffset = 5
        val actual = anchors.shiftToLeft(baseOffset, shiftOffset)
        val expected = listOf(
            OverrideStyleAnchor(start = 10, end = 18 - shiftOffset, style = OverrideStyle.Bold(enabled = true)),
            OverrideStyleAnchor(start = 15 - shiftOffset, end = 25 - shiftOffset, style = OverrideStyle.Italic(enabled = true)),
            OverrideStyleAnchor(start = 15 - shiftOffset, end = 20 - shiftOffset, style = OverrideStyle.Color(color = StyleColor.Dynamic.Default)),
        )
        assertEquals(expected, actual)
    }

    @Test
    fun testShiftToRight() {
        val anchors = listOf(
            OverrideStyleAnchor(start = 10, end = 18, style = OverrideStyle.Bold(enabled = true)),
            OverrideStyleAnchor(start = 15, end = 25, style = OverrideStyle.Italic(enabled = true)),
            OverrideStyleAnchor(start = 15, end = 20, style = OverrideStyle.Color(color = StyleColor.Dynamic.Default)),
        )
        val baseOffset = 11
        val shiftOffset = 5
        val actual = anchors.shiftToRight(baseOffset, shiftOffset)
        val expected = listOf(
            OverrideStyleAnchor(start = 10, end = 18 + shiftOffset, style = OverrideStyle.Bold(enabled = true)),
            OverrideStyleAnchor(start = 15 + shiftOffset, end = 25 + shiftOffset, style = OverrideStyle.Italic(enabled = true)),
            OverrideStyleAnchor(start = 15 + shiftOffset, end = 20 + shiftOffset, style = OverrideStyle.Color(color = StyleColor.Dynamic.Default)),
        )
        assertEquals(expected, actual)
    }

    @Test
    fun testOptimize() {
        val anchors = listOf(
            OverrideStyleAnchor(start = 10, end = 18, style = OverrideStyle.Bold(enabled = true)),
            OverrideStyleAnchor(start = 15, end = 25, style = OverrideStyle.Bold(enabled = true)),
            OverrideStyleAnchor(start = 15, end = 20, style = OverrideStyle.Color(color = StyleColor.Dynamic.Default)),
        )
        val actual = anchors.optimize()
        val expected = listOf(
            OverrideStyleAnchor(start = 10, end = 25, style = OverrideStyle.Bold(enabled = true)),
            OverrideStyleAnchor(start = 15, end = 20, style = OverrideStyle.Color(color = StyleColor.Dynamic.Default)),
        )
        assertEquals(expected, actual)
    }

    @Test
    fun testSortToOptimize() {
        val anchors = listOf(
            OverrideStyleAnchor(start = 0, end = 10, style = OverrideStyle.Bold(enabled = true)),
            OverrideStyleAnchor(start = 1, end = 10, style = OverrideStyle.Italic(enabled = true)),
            OverrideStyleAnchor(start = 5, end = 12, style = OverrideStyle.Bold(enabled = true)),
            OverrideStyleAnchor(start = 2, end = 20, style = OverrideStyle.Color(color = StyleColor.Dynamic.Default)),
        )
        val actual = anchors.sortToOptimize()
        val expected = listOf(
            OverrideStyleAnchor(start = 0, end = 10, style = OverrideStyle.Bold(enabled = true)),
            OverrideStyleAnchor(start = 5, end = 12, style = OverrideStyle.Bold(enabled = true)),
            OverrideStyleAnchor(start = 1, end = 10, style = OverrideStyle.Italic(enabled = true)),
            OverrideStyleAnchor(start = 2, end = 20, style = OverrideStyle.Color(color = StyleColor.Dynamic.Default)),
        )
        assertEquals(expected, actual)

    }
}
