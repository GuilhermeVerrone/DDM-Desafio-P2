package com.example.minhaloja.view

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.minhaloja.R
import com.example.minhaloja.data.AuthRepository
import com.example.minhaloja.data.FavoritoRepository
import com.example.minhaloja.databinding.ActivityFavoritosBinding
import com.example.minhaloja.model.Favorito
import com.google.firebase.firestore.ListenerRegistration

class FavoritosActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoritosBinding
    private val adapter = FavoritoAdapter(::remover)
    private var escuta: ListenerRegistration? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoritosBinding.inflate(layoutInflater)
        setContentView(binding.root)
        configurarEdgeToEdge(binding.root, binding.header)

        binding.recycler.layoutManager = LinearLayoutManager(this)
        binding.recycler.adapter = adapter
        binding.btnVoltar.setOnClickListener { finish() }
    }

    override fun onStart() {
        super.onStart()
        val uid = AuthRepository.uidAtual ?: return sair()
        binding.progress.visibility = View.VISIBLE
        escuta = FavoritoRepository.ouvir(
            uid,
            aoMudar = { lista ->
                binding.progress.visibility = View.GONE
                binding.txtErro.visibility = View.GONE
                binding.txtVazio.visibility = if (lista.isEmpty()) View.VISIBLE else View.GONE
                adapter.submitList(lista)
            },
            aoFalhar = { erro ->
                binding.progress.visibility = View.GONE
                binding.txtErro.setText(mensagemDeErro(erro))
                binding.txtErro.visibility = View.VISIBLE
            }
        )
    }

    override fun onStop() {
        escuta?.remove()
        escuta = null
        super.onStop()
    }

    private fun remover(favorito: Favorito) {
        val uid = AuthRepository.uidAtual ?: return sair()
        FavoritoRepository.remover(uid, favorito.id)
            .addOnFailureListener { Toast.makeText(this, mensagemDeErro(it), Toast.LENGTH_LONG).show() }
    }
}
