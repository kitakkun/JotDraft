package com.github.kitakkun.noteapp.editor.ext

import com.github.kitakkun.noteapp.data.model.OverrideStyle
import com.github.kitakkun.noteapp.data.model.OverrideStyleAnchor
import java.util.Stack

/**
 * split anchor into two anchors at the given offset.
 * if the [offset] is not in the range of the anchor, the anchor will not be split
 * @param offset the offset to split the anchor at
 */
fun List<OverrideStyleAnchor>.splitAt(offset: Int) =
    flatMap { anchor ->
        val anchorRange = anchor.start until anchor.end
        val shouldSplit = offset in anchorRange
        if (shouldSplit) {
            anchor.split(at = offset)
        } else {
            listOf(anchor)
        }
    }

private fun OverrideStyleAnchor.split(at: Int) = listOf(
    copy(start = start, end = at),
    copy(start = at, end = end),
)

internal fun List<OverrideStyleAnchor>.shift(
    shiftOffset: Int,
    shouldShiftEnd: (OverrideStyleAnchor) -> Boolean,
    shouldShiftStart: (OverrideStyleAnchor) -> Boolean,
) = map { anchor ->
    val newStart = if (shouldShiftStart(anchor)) {
        anchor.start + shiftOffset
    } else {
        anchor.start
    }
    val newEnd = if (shouldShiftEnd(anchor)) {
        anchor.end + shiftOffset
    } else {
        anchor.end
    }
    anchor.copy(start = newStart, end = newEnd)
}

/**
 * shift the anchor to the left by the given offset
 * @param baseOffset the base offset to shift the anchor from
 * @param shiftOffset the offset to shift the anchor by
 */
fun List<OverrideStyleAnchor>.shiftToLeft(
    baseOffset: Int,
    shiftOffset: Int,
) = shift(
    shiftOffset = -shiftOffset,
    shouldShiftEnd = { it.end >= baseOffset },
    shouldShiftStart = { it.start >= baseOffset },
)

/**
 * shift the anchor to the right by the given offset
 * @param baseOffset the base offset to shift the anchor from
 * @param shiftOffset the offset to shift the anchor by
 */
fun List<OverrideStyleAnchor>.shiftToRight(
    baseOffset: Int,
    shiftOffset: Int,
) = shift(
    shiftOffset = shiftOffset,
    shouldShiftEnd = { it.end >= baseOffset },
    shouldShiftStart = { it.start >= baseOffset },
)

fun List<OverrideStyleAnchor>.optimize(): List<OverrideStyleAnchor> {
    val sortedAnchors = this.sortToOptimize()
    val optimizeStack = Stack<OverrideStyleAnchor>()

    for (anchor in sortedAnchors) {
        if (optimizeStack.isEmpty()) {
            optimizeStack.add(anchor)
            continue
        }
        val prevAnchor = optimizeStack.peek()
        val canMerge = prevAnchor.style == anchor.style && anchor.start <= prevAnchor.end
        if (canMerge) {
            optimizeStack.pop()
            optimizeStack.add(prevAnchor.copy(end = anchor.end))
        } else {
            optimizeStack.add(anchor)
        }
    }

    return optimizeStack
}

fun List<OverrideStyleAnchor>.optimizeRecursively(): List<OverrideStyleAnchor> {
    val optimized = optimize()
    if (optimized.size == size) {
        return optimized
    }
    return optimized.optimizeRecursively()
}

internal fun List<OverrideStyleAnchor>.sortToOptimize() = this.sortedWith(
    compareBy<OverrideStyleAnchor> { it.start }
        .thenBy { anchor ->
            when (anchor.style) {
                is OverrideStyle.Bold -> 0
                is OverrideStyle.Italic -> 1
                is OverrideStyle.Color -> 2
                else -> 3
            }
        }
)

fun List<OverrideStyleAnchor>.optimize(range: IntRange): List<OverrideStyleAnchor> {
    val nonTargets = filter { it.start !in range || it.end !in range }
    val targets = filter { it.start in range && it.end in range }
    val optimizedTargets = targets.optimize()
    return nonTargets + optimizedTargets
}
