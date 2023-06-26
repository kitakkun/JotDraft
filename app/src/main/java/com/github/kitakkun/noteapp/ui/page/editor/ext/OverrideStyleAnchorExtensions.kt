package com.github.kitakkun.noteapp.ui.page.editor.ext

import com.github.kitakkun.noteapp.ui.page.editor.editmodel.anchor.OverrideStyleAnchor

/**
 * split anchor into two anchors at the given offset
 */
fun List<OverrideStyleAnchor>.splitAt(offset: Int) = flatMap { anchor ->
    val anchorRange = anchor.start until anchor.end
    val shouldSplit = offset in anchorRange
    if (shouldSplit) {
        anchor.split(at = offset)
    } else {
        listOf(anchor)
    }
}

fun OverrideStyleAnchor.split(at: Int) = listOf(
    copy(start = start, end = at),
    copy(start = at, end = end),
)

fun List<OverrideStyleAnchor>.shiftToLeft(
    baseOffset: Int,
    shiftOffset: Int,
) = map {
    shiftAnchorLeft(
        anchor = it,
        baseline = baseOffset,
        shiftOffset = shiftOffset
    )
}

fun List<OverrideStyleAnchor>.shiftToRight(
    baseOffset: Int,
    shiftOffset: Int,
) = map { anchor ->
    shiftAnchorRight(
        anchor = anchor,
        baseOffset = baseOffset,
        shiftOffset = shiftOffset,
    )
}

private fun shiftAnchorLeft(
    anchor: OverrideStyleAnchor,
    baseline: Int,
    shiftOffset: Int,
): OverrideStyleAnchor {
    val shouldShiftStart = anchor.start >= baseline
    val shouldShiftEnd = anchor.end >= baseline
    val newStart = when (shouldShiftStart) {
        true -> anchor.start - shiftOffset
        false -> anchor.start
    }
    val newEnd = when (shouldShiftEnd) {
        true -> anchor.end - shiftOffset
        false -> anchor.end
    }
    return anchor.copy(start = newStart, end = newEnd)
}

private fun shiftAnchorRight(
    anchor: OverrideStyleAnchor,
    baseOffset: Int,
    shiftOffset: Int,
) = when (anchor.start >= baseOffset) {
    true -> anchor.copy(
        start = anchor.start + shiftOffset,
        end = anchor.end + shiftOffset,
    )

    false -> anchor
}
