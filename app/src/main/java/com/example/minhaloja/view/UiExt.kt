package com.example.minhaloja.view

import android.graphics.Color
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.annotation.StringRes
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import com.example.minhaloja.R
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.firestore.FirebaseFirestoreException
import java.text.NumberFormat
import java.util.Locale

private val currency = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("pt-BR"))

fun Double.comoMoeda(): String = currency.format(this)

fun ComponentActivity.configurarEdgeToEdge(root: View, header: View) {
    enableEdgeToEdge(
        statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT),
        navigationBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT)
    )
    val topoOriginal = header.paddingTop
    val baseOriginal = root.paddingBottom
    ViewCompat.setOnApplyWindowInsetsListener(root) { _, insets ->
        val barras = insets.getInsets(WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.displayCutout())
        val ime = insets.getInsets(WindowInsetsCompat.Type.ime())
        root.updatePadding(left = barras.left, right = barras.right, bottom = baseOriginal + maxOf(barras.bottom, ime.bottom))
        header.updatePadding(top = topoOriginal + barras.top)
        insets
    }
}

@StringRes
fun mensagemDeErro(erro: Exception?): Int = when (erro) {
    is FirebaseNetworkException -> R.string.erro_sem_rede
    is FirebaseAuthWeakPasswordException -> R.string.erro_senha_curta
    is FirebaseAuthUserCollisionException -> R.string.erro_email_em_uso
    is FirebaseAuthInvalidUserException, is FirebaseAuthInvalidCredentialsException -> R.string.erro_credenciais
    is FirebaseFirestoreException -> when (erro.code) {
        FirebaseFirestoreException.Code.PERMISSION_DENIED -> R.string.erro_permissao
        FirebaseFirestoreException.Code.UNAVAILABLE -> R.string.erro_sem_rede
        else -> R.string.erro_generico
    }
    else -> R.string.erro_generico
}
