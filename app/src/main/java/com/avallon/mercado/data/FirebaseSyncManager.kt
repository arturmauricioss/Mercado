package com.avallon.mercado.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FirebaseSyncManager(private val appDatabase: AppDatabase) {
    private val auth = FirebaseAuth.getInstance()
    private val database = Firebase.database.reference
    
    private val currentUserId: String?
        get() = auth.currentUser?.uid

    // Referência para os produtos do usuário atual
    private val userProductsRef
        get() = currentUserId?.let { userId ->
            database.child("users").child(userId).child("produtos")
        }

    // Sincroniza dados locais com o Firebase
    suspend fun syncToFirebase(produto: Produto) {
        currentUserId?.let { userId ->
            withContext(Dispatchers.IO) {
                // Se o produto não tem ID, pegamos o próximo ID disponível
                if (produto.id == 0) {
                    val produtoRef = userProductsRef?.push()
                    produtoRef?.setValue(produto)
                } else {
                    userProductsRef?.child(produto.id.toString())?.setValue(produto)
                }
            }
        }
    }

    // Escuta mudanças no Firebase e atualiza o banco local
    fun startSync() {
        userProductsRef?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach { produtoSnapshot ->
                    val produto = produtoSnapshot.getValue(Produto::class.java)
                    produto?.let {
                        // Atualiza o banco local
                        appDatabase.produtoDao().insert(it)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Trate o erro aqui
            }
        })
    }

    // Deleta um produto tanto no Firebase quanto localmente
    suspend fun deleteProduto(produto: Produto) {
        withContext(Dispatchers.IO) {
            userProductsRef?.child(produto.id.toString())?.removeValue()
            // O listener acima vai cuidar de remover do banco local
        }
    }

    // Sincroniza todos os produtos locais com o Firebase
    suspend fun syncAllToFirebase() {
        withContext(Dispatchers.IO) {
            val produtos = appDatabase.produtoDao().getAll()
            produtos.forEach { produto ->
                syncToFirebase(produto)
            }
        }
    }
}
