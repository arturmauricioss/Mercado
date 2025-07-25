package com.avallon.mercado.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.avallon.mercado.data.Produto

@Dao
interface ProdutoDao {
    @Query("SELECT * FROM produtos ORDER BY id DESC")
    fun getAll(): List<Produto>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(produto: Produto)

    @Query("DELETE FROM produtos")
    fun deleteAll()

    @Query("DELETE FROM produtos WHERE id = :produtoId")
    fun deleteById(produtoId: Int)
}
