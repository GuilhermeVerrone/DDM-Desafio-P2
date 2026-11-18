package com.example.minhaloja.data

import com.example.minhaloja.model.Produto
import retrofit2.http.GET

interface ProdutoService {
    @GET("products")
    suspend fun listarProdutos(): List<Produto>
}
