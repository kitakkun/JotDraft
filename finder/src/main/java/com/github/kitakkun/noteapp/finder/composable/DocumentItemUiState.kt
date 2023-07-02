package com.github.kitakkun.noteapp.finder.composable

data class DocumentItemUiState(
    val id: String = "",
    val title: String = "",
    val createdAtText: String = "",
    val isDeleted: Boolean = false,
)
