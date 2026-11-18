package com.example.minhaloja.view

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import com.example.minhaloja.R
import com.example.minhaloja.data.AuthRepository
import com.example.minhaloja.databinding.ActivityLoginBinding
import com.google.android.gms.tasks.Task

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (AuthRepository.estaLogado) {
            abrirProdutos()
            return
        }
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        configurarEdgeToEdge(binding.root, binding.header)

        binding.btnEntrar.setOnClickListener { enviar { email, senha -> AuthRepository.entrar(email, senha) } }
        binding.btnCriarConta.setOnClickListener { enviar { email, senha -> AuthRepository.criarConta(email, senha) } }
        binding.edtSenha.setOnEditorActionListener { _, action, _ ->
            if (action == EditorInfo.IME_ACTION_DONE) binding.btnEntrar.performClick()
            false
        }
    }

    private fun enviar(acao: (String, String) -> Task<*>) {
        val email = binding.edtEmail.text.toString().trim()
        val senha = binding.edtSenha.text.toString()

        binding.tilEmail.error = if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) null else getString(R.string.erro_email_invalido)
        binding.tilSenha.error = when {
            senha.isEmpty() -> getString(R.string.erro_senha_vazia)
            senha.length < 6 -> getString(R.string.erro_senha_curta)
            else -> null
        }
        if (binding.tilEmail.error != null || binding.tilSenha.error != null) return

        carregando(true)
        acao(email, senha)
            .addOnSuccessListener { abrirProdutos() }
            .addOnFailureListener { erro ->
                carregando(false)
                binding.txtErro.setText(mensagemDeErro(erro))
                binding.txtErro.visibility = View.VISIBLE
            }
    }

    private fun carregando(ativo: Boolean) {
        binding.progress.visibility = if (ativo) View.VISIBLE else View.GONE
        binding.btnEntrar.isEnabled = !ativo
        binding.btnCriarConta.isEnabled = !ativo
        if (ativo) binding.txtErro.visibility = View.GONE
    }

    private fun abrirProdutos() {
        startActivity(
            Intent(this, ProdutosActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        )
        finish()
    }
}
