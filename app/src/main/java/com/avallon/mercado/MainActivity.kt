package com.avallon.mercado

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnFornecedores = findViewById<Button>(R.id.btnFornecedores)
        val btnProdutos = findViewById<Button>(R.id.btnProdutos)
        val btnEstoque = findViewById<Button>(R.id.btnEstoque)
        val btnClientes = findViewById<Button>(R.id.btnClientes)
        val btnLog = findViewById<Button>(R.id.btnLog)

        btnFornecedores.setOnClickListener {
            Toast.makeText(this, "Fornecedores clicado", Toast.LENGTH_SHORT).show()
        }

        btnProdutos.setOnClickListener {
            val intent = Intent(this, ProdutosActivity::class.java)
            startActivity(intent)
        }

        btnEstoque.setOnClickListener {
            Toast.makeText(this, "Estoque clicado", Toast.LENGTH_SHORT).show()
        }

        btnClientes.setOnClickListener {
            Toast.makeText(this, "Clientes clicado", Toast.LENGTH_SHORT).show()
        }

        btnLog.setOnClickListener {
            Toast.makeText(this, "Log clicado", Toast.LENGTH_SHORT).show()
        }
    }
}