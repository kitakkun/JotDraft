package com.github.kitakkun.noteapp.ui.page.editor

import com.github.kitakkun.noteapp.ui.page.editor.editmodel.anchor.OverrideStyleAnchor
import com.github.kitakkun.noteapp.ui.page.editor.editmodel.style.OverrideStyle

fun OverrideStyleAnchor.split(at: Int) = listOf(
    copy(start = start, end = at),
    copy(start = at, end = end),
)

fun List<OverrideStyleAnchor>.mapWithLeftShift(
    cursorPos: Int,
    shiftOffset: Int,
) = map {
    shiftAnchorLeft(
        anchor = it,
        baseline = cursorPos,
        shiftOffset = shiftOffset
    )
}

fun List<OverrideStyleAnchor>.mapWithRightShift(
    cursorPos: Int,
    shiftOffset: Int,
    currentInsertionStyles: List<OverrideStyle>,
) = map {
    shiftAnchorRight(
        anchor = it,
        baseline = cursorPos,
        shiftOffset = shiftOffset,
        currentInsertionStyles = currentInsertionStyles,
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

// 既存のアンカーを右へシフトする
private fun shiftAnchorRight(
    anchor: OverrideStyleAnchor,
    baseline: Int,
    shiftOffset: Int,
    currentInsertionStyles: List<OverrideStyle>,
): OverrideStyleAnchor {
    // カーソル位置より左側のstartはシフトしない
    val shouldShiftStart = anchor.start >= baseline
    // カーソル位置より左側のendはシフトしない
    val shouldShiftEnd =
        anchor.end > baseline || (anchor.end == baseline && anchor.style in currentInsertionStyles)

    val newStart = when (shouldShiftStart) {
        true -> anchor.start + shiftOffset
        false -> anchor.start
    }
    val newEnd = when (shouldShiftEnd) {
        true -> anchor.end + shiftOffset
        false -> anchor.end
    }

    return anchor.copy(
        start = newStart,
        end = newEnd
    )
}
