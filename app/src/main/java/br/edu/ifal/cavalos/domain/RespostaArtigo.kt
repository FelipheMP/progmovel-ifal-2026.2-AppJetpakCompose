package br.edu.ifal.cavalos.domain

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RespostaArtigo(val query: ConsultaArtigo)

@JsonClass(generateAdapter = true)
data class ConsultaArtigo(val pages: List<PaginaArtigo>)

@JsonClass(generateAdapter = true)
data class PaginaArtigo(val extract: String? = null)

data class SecaoArtigo(val titulo: String, val texto: String)

private val cabecalhoWiki = Regex("""^(={2,6})[ \t]*(.+?)[ \t]*\1[ \t]*$""", RegexOption.MULTILINE)
private val secoesDaRaca = setOf("Aparência geral", "Ação", "Andamento")

internal fun selecionarSecoes(extrato: String): List<SecaoArtigo> {
    val cabecalhos = cabecalhoWiki.findAll(extrato).toList()
    val secoes = mutableListOf<SecaoArtigo>()
    // Cada cabeçalho marca o início de uma seção. O próximo marca o fim do texto.
    for (indice in cabecalhos.indices) {
        val cabecalho = cabecalhos[indice]
        val titulo = cabecalho.groupValues[2]
        if (titulo !in secoesDaRaca) continue

        val inicio = cabecalho.range.last + 1
        val fim = if (indice + 1 < cabecalhos.size) {
            cabecalhos[indice + 1].range.first
        } else {
            extrato.length
        }
        val texto = extrato.substring(inicio, fim).trim()
        if (texto.isNotEmpty()) {
            secoes.add(SecaoArtigo(titulo, texto))
        }
    }
    return secoes
}
