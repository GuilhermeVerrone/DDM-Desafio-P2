package com.example.minhaloja.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.minhaloja.R
import com.example.minhaloja.data.FavoritoRepository
import com.example.minhaloja.data.AuthRepository
import com.example.minhaloja.data.RetrofitClient
import com.example.minhaloja.databinding.ActivityProdutosBinding
import com.example.minhaloja.model.Produto
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.launch

class ProdutosActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProdutosBinding
    private val adapter = ProdutoAdapter(::salvarFavorito)
    private var escuta: ListenerRegistration? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!AuthRepository.estaLogado) {
            sair()
            return
        }
        binding = ActivityProdutosBinding.inflate(layoutInflater)
        setContentView(binding.root)
        configurarEdgeToEdge(binding.root, binding.header)

        binding.recycler.layoutManager = LinearLayoutManager(this)
        binding.recycler.adapter = adapter
        binding.btnFavoritos.setOnClickListener { startActivity(Intent(this, FavoritosActivity::class.java)) }
        binding.btnSair.setOnClickListener { sair() }
        binding.btnTentarDeNovo.setOnClickListener { carregarProdutos() }

        carregarProdutos()
    }

    override fun onStart() {
        super.onStart()
        val uid = AuthRepository.uidAtual ?: return
        escuta = FavoritoRepository.ouvir(uid, { adapter.favoritos = it.map { f -> f.id }.toSet() }, {})
    }

    override fun onStop() {
        escuta?.remove()
        escuta = null
        super.onStop()
    }

    private fun carregarProdutos() {
        mostrarEstado(carregando = true)
        lifecycleScope.launch {
            try {
                val produtos = RetrofitClient.produtos.listarProdutos()
                adapter.submitList(produtos)
                mostrarEstado(vazio = produtos.isEmpty())
            } catch (e: kotlinx.coroutines.CancellationException) {
                throw e
            } catch (e: Exception) {
                mostrarEstado(erro = true)
            }
        }
    }

    private fun mostrarEstado(carregando: Boolean = false, erro: Boolean = false, vazio: Boolean = false) {
        binding.progress.visibility = if (carregando) View.VISIBLE else View.GONE
        binding.grupoErro.visibility = if (erro) View.VISIBLE else View.GONE
        binding.txtVazio.visibility = if (vazio) View.VISIBLE else View.GONE
        binding.recycler.visibility = if (erro) View.GONE else View.VISIBLE
    }

    private fun salvarFavorito(produto: Produto) {
        val uid = AuthRepository.uidAtual ?: return sair()
        FavoritoRepository.salvar(uid, produto)
            .addOnSuccessListener { Toast.makeText(this, R.string.favorito_salvo_msg, Toast.LENGTH_SHORT).show() }
            .addOnFailureListener { Toast.makeText(this, mensagemDeErro(it), Toast.LENGTH_LONG).show() }
    }
}
