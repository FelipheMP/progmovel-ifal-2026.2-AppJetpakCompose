package br.edu.ifal.cavalos.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import br.edu.ifal.cavalos.db.treinosDeExemplo
import br.edu.ifal.cavalos.domain.Treino
import br.edu.ifal.cavalos.ui.components.CardTreino
import br.edu.ifal.cavalos.ui.theme.CavalosTheme
import br.edu.ifal.cavalos.ui.viewmodel.EstadoTreinos
import br.edu.ifal.cavalos.ui.viewmodel.TreinosViewModel

@Composable
fun TreinosScreen(modelo: TreinosViewModel = viewModel()) {
    // by permite ler o valor observado como uma variável comum; o Compose reage às mudanças.
    val estado by modelo.estado.collectAsStateWithLifecycle()
    when (val atual = estado) {
        EstadoTreinos.Carregando -> AvisoCentral { CircularProgressIndicator() }
        EstadoTreinos.Erro -> AvisoCentral { Text("Não foi possível ler os treinos.") }
        is EstadoTreinos.Sucesso -> ListaTreinos(atual.treinos)
    }
}

@Composable
private fun ListaTreinos(treinos: List<Treino>) {
    // LazyColumn monta os itens conforme ficam visíveis, sem criar toda a lista de uma vez.
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text("Treinos do dia", style = MaterialTheme.typography.headlineMedium)
                Text(
                    "Rotina de exemplo",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
        if (treinos.isEmpty()) {
            item { Text("Nenhum treino disponível.") }
        }
        // A chave identifica cada treino mesmo se a posição dele mudar.
        items(treinos, key = { it.id }) { treino ->
            CardTreino(treino)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TreinosPreview() {
    CavalosTheme {
        ListaTreinos(treinosDeExemplo)
    }
}
