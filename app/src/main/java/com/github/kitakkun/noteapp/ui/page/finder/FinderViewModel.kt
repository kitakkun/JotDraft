package com.github.kitakkun.noteapp.ui.page.finder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.github.kitakkun.noteapp.data.DocumentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FinderViewModel(
    private val documentRepository: DocumentRepository,
    private val navController: NavController,
) : ViewModel() {
    private val mutableUiState = MutableStateFlow(FinderUiState())
    val uiState = mutableUiState.asStateFlow()

    fun fetchDocuments() = viewModelScope.launch {
        val documents = documentRepository.fetchDocuments()
        mutableUiState.value = uiState.value.copy(
            documents = documents.map { document ->
                DocumentItemUiState(
                    title = document.title,
                    createdAtText = "",
                )
            }
        )
    }

    // 言ってることとやってることが噛み合ってないのであとで直す
    // 新規ドキュメントを作成して，編集画面に遷移したい
    fun createNewDocument() {
        navController.navigate("noteEdit")
    }
}
