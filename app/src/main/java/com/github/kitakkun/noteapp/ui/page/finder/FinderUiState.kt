package com.github.kitakkun.noteapp.ui.page.finder

data class FinderUiState(
    val searchWord: String = "",
    val documents: List<DocumentItemUiState> = emptyList(),
    val isEditMode: Boolean = false,
    val showConfirmRemovalDialog: Boolean = false,
    val selectedDocumentId: String? = null,
) {

    companion object {
        internal fun buildPreviewData() = FinderUiState(
            searchWord = "",
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
