package com.avallon.mercado.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "produtos")
data class Produto(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    
    @ColumnInfo(name = "nome")
    var nome: String = "",
    
    @ColumnInfo(name = "imagem_uri")
    var imagemUri: String = ""
)
