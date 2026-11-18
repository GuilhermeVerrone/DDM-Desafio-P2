package com.example.minhaloja.model

import java.util.Date

data class Favorito(
    val id: Int,
    val titulo: String,
    val preco: Double,
    val categoria: String,
    val salvoEm: Date?
)
