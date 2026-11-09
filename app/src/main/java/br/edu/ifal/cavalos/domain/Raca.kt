package br.edu.ifal.cavalos.domain

import com.squareup.moshi.JsonClass

// Os nomes correspondem às chaves do JSON; thumbnail é opcional na resposta.
@JsonClass(generateAdapter = true)
data class Raca(
    val title: String,
    val extract: String,
    val thumbnail: Miniatura? = null
)

@JsonClass(generateAdapter = true)
data class Miniatura(val source: String)
