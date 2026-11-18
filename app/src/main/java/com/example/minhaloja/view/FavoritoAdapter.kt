package com.example.minhaloja.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.minhaloja.databinding.ItemFavoritoBinding
import com.example.minhaloja.model.Favorito

class FavoritoAdapter(
    private val aoRemover: (Favorito) -> Unit
) : ListAdapter<Favorito, FavoritoAdapter.Holder>(Diff) {

    class Holder(val binding: ItemFavoritoBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        Holder(ItemFavoritoBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val favorito = getItem(position)
        with(holder.binding) {
            txtTitulo.text = favorito.titulo
            txtCategoria.text = favorito.categoria
            txtPreco.text = favorito.preco.comoMoeda()
            btnRemover.setOnClickListener { aoRemover(favorito) }
        }
    }

    private object Diff : DiffUtil.ItemCallback<Favorito>() {
        override fun areItemsTheSame(a: Favorito, b: Favorito) = a.id == b.id
        override fun areContentsTheSame(a: Favorito, b: Favorito) = a == b
    }
}
