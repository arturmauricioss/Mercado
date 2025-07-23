package com.exemplo.meuapp

import android.net.Uri
import android.view.LayoutInflater
import java.io.File
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.exemplo.meuapp.data.Produto

class ProdutoAdapter : ListAdapter<Produto, ProdutoAdapter.ProdutoViewHolder>(ProdutoDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProdutoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_produto, parent, false)
        return ProdutoViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProdutoViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ProdutoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.imageViewProdutoItem)
        private val textViewNome: TextView = itemView.findViewById(R.id.textViewNomeProduto)

        fun bind(produto: Produto) {
            textViewNome.text = produto.nome
            if (produto.imagemUri.isNotEmpty()) {
                try {
                    val file = File(produto.imagemUri)
                    if (file.exists()) {
                        imageView.setImageURI(Uri.fromFile(file))
                    } else {
                        imageView.setImageResource(android.R.drawable.ic_menu_gallery)
                    }
                } catch (e: Exception) {
                    imageView.setImageResource(android.R.drawable.ic_menu_gallery)
                }
            } else {
                imageView.setImageResource(android.R.drawable.ic_menu_gallery)
            }
        }
    }

    class ProdutoDiffCallback : DiffUtil.ItemCallback<Produto>() {
        override fun areItemsTheSame(oldItem: Produto, newItem: Produto): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Produto, newItem: Produto): Boolean {
            return oldItem == newItem
        }
    }
}
