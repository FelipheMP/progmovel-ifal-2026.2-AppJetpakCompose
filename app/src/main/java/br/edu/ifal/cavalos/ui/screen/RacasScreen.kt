package br.edu.ifal.cavalos.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import br.edu.ifal.cavalos.api.IDENTIFICACAO_APP
import br.edu.ifal.cavalos.domain.Raca
import br.edu.ifal.cavalos.ui.theme.CavalosTheme
import br.edu.ifal.cavalos.ui.viewmodel.EstadoDetalhes
import br.edu.ifal.cavalos.ui.viewmodel.EstadoRaca
import br.edu.ifal.cavalos.ui.viewmodel.RacasViewModel
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest

@Composable
fun RacasScreen(modelo: RacasViewModel = viewModel()) {
    // Observa o ViewModel enquanto a tela está ativa. Mudanças redesenham a interface.
    val estado by modelo.estado.collectAsStateWithLifecycle()
    when (val atual = estado) {
        EstadoRaca.Carregando -> AvisoCentral { CircularProgressIndicator() }
        EstadoRaca.Erro -> AvisoCentral {
            Text("Não foi possível carregar a raça.")
            Button(onClick = modelo::tentarNovamente) {
                Text("Tentar novamente")
            }
        }
        is EstadoRaca.Sucesso -> ResumoRaca(atual.raca, atual.detalhes, modelo::tentarDetalhesNovamente)
    }
}

@Composable
private fun ResumoRaca(raca: Raca, detalhes: EstadoDetalhes, tentarDetalhes: () -> Unit) {
    val navegador = LocalUriHandler.current
    // Column empilha elementos. Modifier configura tamanho, rolagem e espaçamento.
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        val foto = raca.thumbnail?.source
        if (!foto.isNullOrBlank()) {
            val contexto = LocalContext.current
            // Recria o pedido apenas se a foto ou o contexto mudar; preserva o header do app.
            val pedido = remember(contexto, foto) {
                ImageRequest.Builder(contexto)
                    .data(foto)
                    .addHeader("User-Agent", IDENTIFICACAO_APP)
                    .build()
            }
            SubcomposeAsyncImage(
                model = pedido,
                contentDescription = "Cavalo da raça ${raca.title}",
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.7f)
                    .clip(MaterialTheme.shapes.large)
                    .background(MaterialTheme.colorScheme.surfaceContainer),
                contentScale = ContentScale.Crop,
                loading = {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                },
                error = {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Imagem indisponível")
                    }
                }
            )
        }
        Text(raca.title, style = MaterialTheme.typography.headlineMedium)
        Text(raca.extract, style = MaterialTheme.typography.bodyLarge)
        TextButton(onClick = { navegador.openUri("https://pt.wikipedia.org/wiki/Manga-larga_marchador") }) {
            Text("Ver artigo na Wikipédia")
        }
        // Os detalhes têm estado próprio: uma falha aqui não apaga o resumo já recebido.
        when (detalhes) {
            EstadoDetalhes.Carregando -> Text("Carregando detalhes da raça…")
            EstadoDetalhes.Erro -> {
                Text("Não foi possível carregar os detalhes da raça.")
                TextButton(onClick = tentarDetalhes) { Text("Tentar carregar detalhes novamente") }
            }
            is EstadoDetalhes.Sucesso -> detalhes.secoes.forEach { secao ->
                Text(
                    secao.titulo,
                    modifier = Modifier.padding(top = 8.dp).semantics { heading() },
                    style = MaterialTheme.typography.titleLarge
                )
                Text(secao.texto, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

// Uma função @Composable pode montar interface e receber outra interface como conteúdo.
@Composable
internal fun AvisoCentral(conteudo: @Composable () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        conteudo()
    }
}

// Preview usa dados locais apenas no editor, sem fazer chamadas à API.
@Preview(showBackground = true)
@Composable
private fun RacasPreview() {
    CavalosTheme {
        ResumoRaca(
            raca = Raca(
                title = "Manga-larga marchador",
                extract = "Raça brasileira desenvolvida na região do Rio das Mortes, em Minas Gerais."
            ),
            detalhes = EstadoDetalhes.Sucesso(emptyList()),
            tentarDetalhes = {}
        )
    }
}
