package com.exemplo.meuapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.exemplo.meuapp.data.Produto

@Dao
interface ProdutoDao {
    @Query("SELECT * FROM produtos ORDER BY id DESC")
    fun getAll(): List<Produto>

    @Insert
    fun insert(produto: Produto)

    @Query("DELETE FROM produtos")
    fun deleteAll()
}
