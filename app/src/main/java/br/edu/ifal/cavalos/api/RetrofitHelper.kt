package br.edu.ifal.cavalos.api

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object RetrofitHelper {
    // object compartilha a configuração; lazy cria o Retrofit no primeiro acesso.
    val wikipediaApi: WikipediaApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://pt.wikipedia.org/")
            // Moshi transforma o JSON recebido nos modelos de domain.
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(WikipediaApi::class.java)
    }
}
