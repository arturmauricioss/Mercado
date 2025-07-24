package com.avallon.mercado

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import java.io.File
import java.io.FileOutputStream
import java.util.UUID
import com.avallon.mercado.data.AppDatabase
import com.avallon.mercado.data.Produto
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AdicionarProdutoActivity : AppCompatActivity() {
    private lateinit var imageViewProduto: ImageView
    private lateinit var editTextNome: TextInputEditText
    private var selectedImageUri: Uri? = null

    private val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            selectedImageUri = it
            imageViewProduto.setImageURI(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adicionar_produto)

        imageViewProduto = findViewById(R.id.imageViewProduto)
        editTextNome = findViewById(R.id.editTextNome)

        findViewById<Button>(R.id.buttonSelecionarImagem).setOnClickListener {
            getContent.launch("image/*")
        }

        findViewById<Button>(R.id.buttonSalvar).setOnClickListener {
            salvarProduto()
        }
    }

    private fun salvarImagemInterna(uri: Uri): String? {
        return try {
            val inputStream = contentResolver.openInputStream(uri)
            val fileName = "produto_${UUID.randomUUID()}.jpg"
            val imagesDir = File(filesDir, "images").apply { mkdirs() }
            val outputFile = File(imagesDir, fileName)
            
            FileOutputStream(outputFile).use { outputStream ->
                inputStream?.copyTo(outputStream)
            }
            
            outputFile.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun salvarProduto() {
        try {
            val nome = editTextNome.text?.toString()
            if (nome.isNullOrBlank()) {
                Toast.makeText(this, "Digite o nome do produto", Toast.LENGTH_SHORT).show()
                return
            }

            val imagemPath = selectedImageUri?.let { salvarImagemInterna(it) } ?: ""
            
            val produto = Produto(
                nome = nome.trim(),
                imagemUri = imagemPath
            )
            
            AppDatabase.getDatabase(this).produtoDao().insert(produto)
            
            Toast.makeText(this, "Produto salvo com sucesso!", Toast.LENGTH_SHORT).show()
            setResult(Activity.RESULT_OK)
            finish()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this,
                "Erro ao salvar: ${e.localizedMessage}",
                Toast.LENGTH_LONG).show()
        }
    }
}
