package com.github.kitakkun.noteapp.ui.page.finder

data class FinderUiState(
    val documents: List<DocumentItemUiState> = emptyList(),
) {
    companion object {
        internal fun buildPreviewData() = FinderUiState(
            documents = listOf(
                DocumentItemUiState(
                    title = "Document 1",
                    createdAtText = "2021/01/01 00:00:00",
                ),
                DocumentItemUiState(
                    title = "Document 2",
                    createdAtText = "2021/01/02 00:00:00",
                ),
                DocumentItemUiState(
                    title = "Document 3",
                    createdAtText = "2021/01/03 00:00:00",
                ),
            )
        )
    }
}
