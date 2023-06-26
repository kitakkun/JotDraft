package com.github.kitakkun.noteapp.ui.page.editor.ext

import com.github.kitakkun.noteapp.ui.page.editor.editmodel.anchor.BaseStyleAnchor
import com.github.kitakkun.noteapp.ui.page.editor.editmodel.style.BaseStyle

/**
 * @param deleteCursorLine base cursor position for deletion
 * @param deleteLines number of lines to delete
 */
fun List<BaseStyleAnchor>.deleteLinesAndShiftUp(
    deleteCursorLine: Int,
    deleteLines: Int
) = this
    // 削除範囲の行に含まれるアンカーを削除
    .filterNot {
        it.line in deleteCursorLine until deleteCursorLine + deleteLines
    }
    // 削除位置より下にあったアンカーを上へ移動する
    .map {
        if (it.line >= deleteCursorLine + deleteLines) {
            it.copy(line = it.line - deleteLines)
        } else {
            it
        }
    }

/**
 * 指定されたBaseStyleをinsert cursorを基準にinsertLines行挿入し，下に存在するアンカーを挿入位置より下に移動する
 */
fun List<BaseStyleAnchor>.insertNewAnchorsAndShiftDown(
    baseStyle: BaseStyle,
    insertCursorLine: Int,
    insertLines: Int
): List<BaseStyleAnchor> {
    // 新規に挿入する必要のあるアンカーリスト
    val newAnchors = (insertCursorLine until insertCursorLine + insertLines).map {
        BaseStyleAnchor(it, baseStyle)
    }
    return (newAnchors + this
        // 挿入位置より下にあったアンカーを下へ移動する
        .map {
            if (it.line >= insertCursorLine) {
                it.copy(line = it.line + insertLines)
            } else {
                it
            }
        }).distinctBy { it.line }
}
