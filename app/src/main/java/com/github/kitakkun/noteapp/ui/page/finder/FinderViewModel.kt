package com.github.kitakkun.noteapp.ui.page.finder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.kitakkun.noteapp.data.DocumentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FinderViewModel(
    private val documentRepository: DocumentRepository,
) : ViewModel() {
    private val mutableUiState = MutableStateFlow(FinderUiState())
    val uiState = mutableUiState.asStateFlow()

    fun fetchDocuments() = viewModelScope.launch {
        val documents = documentRepository.fetchDocuments()
        mutableUiState.value = uiState.value.copy(
            documents = documents.map { document ->
                DocumentItemUiState(
                    id = document.id,
                    title = document.title,
                    createdAtText = "",
                )
            }
        )
    }

    fun updateSearchWord(word: String) {
        mutableUiState.update {
            it.copy(searchWord = word)
        }
    }

    fun toggleEditMode() {
        mutableUiState.update {
            it.copy(isEditMode = !it.isEditMode)
        }
    }

    fun openConfirmRemovalDialog(id: String) {
        mutableUiState.update {
            it.copy(
                showConfirmRemovalDialog = true,
                selectedDocumentId = id,
            )
        }
    }

    fun removeDocument() = viewModelScope.launch {
        val documentId = uiState.value.selectedDocumentId ?: return@launch
        documentRepository.deleteDocumentById(documentId)
        mutableUiState.update {
            it.copy(
                documents = it.documents.map { document ->
                    if (document.id != documentId) {
                        document
                    } else {
                        document.copy(isDeleted = true)
                    }
                }
            )
        }
    }

    fun closeConfirmRemovalDialog() {
        mutableUiState.update {
            it.copy(
                showConfirmRemovalDialog = false,
                selectedDocumentId = null,
            )
        }
    }
}
