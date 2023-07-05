package com.github.kitakkun.noteapp.editor.ext

import com.github.kitakkun.noteapp.data.model.OverrideStyle
import com.github.kitakkun.noteapp.data.model.OverrideStyleAnchor

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

/**
 * shift the anchor to the left by the given offset
 * @param baseOffset the base offset to shift the anchor from
 * @param shiftOffset the offset to shift the anchor by
 */
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

/**
 * shift the anchor to the right by the given offset
 * @param baseOffset the base offset to shift the anchor from
 * @param shiftOffset the offset to shift the anchor by
 */
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

fun List<OverrideStyleAnchor>.optimize(): List<OverrideStyleAnchor> {
    // まず、要素を始点とスタイルの種類でソートします
    val sorted = this.sortedWith(
        compareBy<OverrideStyleAnchor> { it.start }
            .thenBy {
                when (it.style) {
                    is OverrideStyle.Bold -> 0
                    is OverrideStyle.Italic -> 1
                    else -> 2
                }
            }
    )

    // 最適化後のリストを保持するための変数を用意します
    val optimized = mutableListOf<OverrideStyleAnchor>()

    // ソートされたリストを反復処理します
    for (anchor in sorted) {
        // 最適化後のリストが空でないかつ、直前の要素と現在の要素が結合可能な場合
        if (optimized.isNotEmpty() &&
            optimized.last().style == anchor.style &&
            optimized.last().end == anchor.start
        ) {

            // 直前の要素を新しい範囲で更新します
            val last = optimized.removeLast()
            optimized.add(last.copy(end = anchor.end))
        } else {
            // それ以外の場合は現在の要素を最適化後のリストに追加します
            optimized.add(anchor)
        }
    }

    return optimized
}
