package com.avallon.mercado

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.avallon.mercado.data.AppDatabase
import com.avallon.mercado.data.Produto
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProdutosActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var produtoAdapter: ProdutoAdapter
    private lateinit var db: AppDatabase

    private val addProdutoLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            carregarProdutos()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_produtos)
        
        supportActionBar?.title = "Produtos"

        db = AppDatabase.getDatabase(this)
        setupRecyclerView()
        setupFab()
        carregarProdutos()
    }

    private fun setupRecyclerView() {
        recyclerView = findViewById(R.id.recyclerViewProdutos)
        produtoAdapter = ProdutoAdapter()
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@ProdutosActivity)
            adapter = produtoAdapter
        }
    }

    private fun setupFab() {
        findViewById<FloatingActionButton>(R.id.fabAddProduto).setOnClickListener {
            val intent = Intent(this, AdicionarProdutoActivity::class.java)
            addProdutoLauncher.launch(intent)
        }
    }

    private fun carregarProdutos() {
        try {
            val produtos = db.produtoDao().getAll()
            produtoAdapter.submitList(produtos)
            
            if (produtos.isEmpty()) {
                Toast.makeText(this, "Nenhum produto cadastrado", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, 
                "Erro ao carregar produtos: ${e.localizedMessage}", 
                Toast.LENGTH_LONG).show()
        }
    }
}
