package com.avallon.mercado.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.avallon.mercado.data.Produto
import com.avallon.mercado.data.ProdutoDao

@Database(entities = [Produto::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun produtoDao(): ProdutoDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "produtos_db" // Novo nome para o banco
                )
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries() // Temporariamente permitir queries na thread principal
                .build()

                // Limpar qualquer instância anterior
                INSTANCE?.close()
                INSTANCE = instance
                instance
            }
        }
    }
}
