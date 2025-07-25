package com.avallon.mercado.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.avallon.mercado.data.AppDatabase
import com.avallon.mercado.data.FirebaseSyncManager
import com.avallon.mercado.data.Produto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProdutoViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AppDatabase.getDatabase(application)
    private val syncManager = FirebaseSyncManager(database)
    
    private val _produtos = MutableStateFlow<List<Produto>>(emptyList())
    val produtos: StateFlow<List<Produto>> = _produtos.asStateFlow()

    init {
        // Inicia a sincronização com o Firebase
        syncManager.startSync()
        // Carrega produtos locais
        loadProdutos()
    }

    private fun loadProdutos() {
        viewModelScope.launch {
            val listaProdutos = database.produtoDao().getAll()
            _produtos.value = listaProdutos
        }
    }

    fun addProduto(produto: Produto) {
        viewModelScope.launch {
            // Salva localmente
            database.produtoDao().insert(produto)
            // Sincroniza com Firebase
            syncManager.syncToFirebase(produto)
            // Recarrega a lista
            loadProdutos()
        }
    }

    fun deleteProduto(produto: Produto) {
        viewModelScope.launch {
            database.produtoDao().deleteById(produto.id)
            syncManager.deleteProduto(produto)
            loadProdutos()
        }
    }
}
