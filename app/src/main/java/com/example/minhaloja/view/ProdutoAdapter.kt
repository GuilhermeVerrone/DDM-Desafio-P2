package com.example.minhaloja.view

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.minhaloja.R
import com.example.minhaloja.databinding.ItemProdutoBinding
import com.example.minhaloja.model.Produto

class ProdutoAdapter(
    private val aoTocar: (Produto) -> Unit
) : ListAdapter<Produto, ProdutoAdapter.Holder>(Diff) {

    @SuppressLint("NotifyDataSetChanged")
    var favoritos: Set<Int> = emptySet()
        set(valor) {
            field = valor
            notifyDataSetChanged()
        }

    inner class Holder(val binding: ItemProdutoBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        Holder(ItemProdutoBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val produto = getItem(position)
        with(holder.binding) {
            txtTitulo.text = produto.title
            txtCategoria.text = produto.category
            txtPreco.text = produto.price.comoMoeda()
            imgProduto.load(produto.image)
            val favorito = produto.id in favoritos
            imgCoracao.setImageResource(if (favorito) R.drawable.ic_favorite else R.drawable.ic_favorite_border)
            imgCoracao.contentDescription = root.context.getString(if (favorito) R.string.favorito_salvo else R.string.favorito_salvar)
            root.setOnClickListener { aoTocar(produto) }
        }
    }

    private object Diff : DiffUtil.ItemCallback<Produto>() {
        override fun areItemsTheSame(a: Produto, b: Produto) = a.id == b.id
        override fun areContentsTheSame(a: Produto, b: Produto) = a == b
    }
}
