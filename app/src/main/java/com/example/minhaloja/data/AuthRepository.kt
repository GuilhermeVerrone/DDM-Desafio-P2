package com.example.minhaloja.data

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth

object AuthRepository {

    private val auth get() = FirebaseAuth.getInstance()

    val uidAtual: String? get() = auth.currentUser?.uid

    val estaLogado: Boolean get() = auth.currentUser != null

    fun entrar(email: String, senha: String): Task<AuthResult> =
        auth.signInWithEmailAndPassword(email, senha)

    fun criarConta(email: String, senha: String): Task<AuthResult> =
        auth.createUserWithEmailAndPassword(email, senha)

    fun sair() = auth.signOut()
}
