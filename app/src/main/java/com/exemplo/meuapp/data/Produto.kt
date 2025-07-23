package com.exemplo.meuapp.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "produtos")
data class Produto(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    
    @ColumnInfo(name = "nome", defaultValue = "")
    val nome: String = "",
    
    @ColumnInfo(name = "imagem_uri", defaultValue = "")
    val imagemUri: String = ""
)
