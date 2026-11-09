package br.edu.ifal.cavalos.api

import br.edu.ifal.cavalos.domain.Raca
import br.edu.ifal.cavalos.domain.RespostaArtigo
import retrofit2.http.GET
import retrofit2.http.Headers

internal const val IDENTIFICACAO_APP = "Cavalos-IFAL/1.0"

interface WikipediaApi {
    @Headers("User-Agent: $IDENTIFICACAO_APP")
    @GET("api/rest_v1/page/summary/Mangalarga_Marchador")
    suspend fun consultarRaca(): Raca

    @Headers("User-Agent: $IDENTIFICACAO_APP")
    @GET(
        "w/api.php?action=query&prop=extracts&explaintext=1&exsectionformat=wiki" +
            "&format=json&formatversion=2&redirects=1&titles=Mangalarga_Marchador"
    )
    suspend fun consultarArtigo(): RespostaArtigo
}
