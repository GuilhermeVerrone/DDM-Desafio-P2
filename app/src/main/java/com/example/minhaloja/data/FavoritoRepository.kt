package com.example.minhaloja.data

import com.example.minhaloja.model.Favorito
import com.example.minhaloja.model.Produto
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.DocumentSnapshot.ServerTimestampBehavior

object FavoritoRepository {

    private val db get() = FirebaseFirestore.getInstance()

    private fun colecao(uid: String) =
        db.collection("usuarios").document(uid).collection("favoritos")

    fun salvar(uid: String, produto: Produto): Task<Void> =
        colecao(uid).document(produto.id.toString()).set(
            hashMapOf(
                "id" to produto.id,
                "titulo" to produto.title,
                "preco" to produto.price,
                "categoria" to produto.category,
                "salvoEm" to FieldValue.serverTimestamp()
            )
        )

    fun remover(uid: String, id: Int): Task<Void> =
        colecao(uid).document(id.toString()).delete()

    fun ouvir(
        uid: String,
        aoMudar: (List<Favorito>) -> Unit,
        aoFalhar: (Exception) -> Unit
    ): ListenerRegistration =
        colecao(uid).orderBy("salvoEm", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, erro ->
                if (erro != null) aoFalhar(erro) else aoMudar(snapshot!!.documents.mapNotNull(::paraFavorito))
            }

    private fun paraFavorito(doc: DocumentSnapshot): Favorito? {
        val id = doc.getLong("id")?.toInt() ?: return null
        return Favorito(
            id = id,
            titulo = doc.getString("titulo").orEmpty(),
            preco = doc.getDouble("preco") ?: 0.0,
            categoria = doc.getString("categoria").orEmpty(),
            salvoEm = doc.getTimestamp("salvoEm", ServerTimestampBehavior.ESTIMATE)?.toDate()
        )
    }
}
