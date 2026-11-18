package com.example.minhaloja.view

import android.app.Activity
import android.content.Intent
import com.example.minhaloja.data.AuthRepository

fun Activity.sair() {
    AuthRepository.sair()
    startActivity(
        Intent(this, LoginActivity::class.java)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
    )
    finish()
}
