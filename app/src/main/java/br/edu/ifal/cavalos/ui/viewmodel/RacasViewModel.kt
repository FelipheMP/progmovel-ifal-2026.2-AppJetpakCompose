package br.edu.ifal.cavalos.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.edu.ifal.cavalos.api.RetrofitHelper
import br.edu.ifal.cavalos.domain.Raca
import br.edu.ifal.cavalos.domain.SecaoArtigo
import br.edu.ifal.cavalos.domain.selecionarSecoes
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// sealed limita as possibilidades; o when da tela trata carregamento, sucesso e erro.
sealed interface EstadoRaca {
    data object Carregando : EstadoRaca
    data class Sucesso(val raca: Raca, val detalhes: EstadoDetalhes) : EstadoRaca
    data object Erro : EstadoRaca
}

sealed interface EstadoDetalhes {
    data object Carregando : EstadoDetalhes
    data class Sucesso(val secoes: List<SecaoArtigo>) : EstadoDetalhes
    data object Erro : EstadoDetalhes
}

class RacasViewModel : ViewModel() {
    // Só o ViewModel altera o fluxo; a tela recebe uma versão somente para leitura.
    private val _estado = MutableStateFlow<EstadoRaca>(EstadoRaca.Carregando)
    val estado = _estado.asStateFlow()

    // Executado uma vez por instância, evitando consultas a cada recomposição.
    init {
        carregar()
    }

    fun tentarNovamente() {
        if (_estado.value == EstadoRaca.Erro) carregar()
    }

    fun tentarDetalhesNovamente() {
        // Só há detalhes para repetir quando já temos um resumo carregado.
        val atual = _estado.value as? EstadoRaca.Sucesso ?: return
        if (atual.detalhes == EstadoDetalhes.Erro) {
            viewModelScope.launch { carregarDetalhes(atual.raca) }
        }
    }

    private fun carregar() {
        // A coroutine é cancelada quando o ViewModel é descartado.
        viewModelScope.launch {
            _estado.value = EstadoRaca.Carregando
            try {
                val raca = RetrofitHelper.wikipediaApi.consultarRaca()
                carregarDetalhes(raca)
            } catch (erro: CancellationException) {
            // Cancelamento é parte do ciclo de vida, não uma falha de conexão.
                throw erro
            } catch (_: Exception) {
                _estado.value = EstadoRaca.Erro
            }
        }
    }

    // suspend permite aguardar a rede sem bloquear a interface; Retrofit faz a operação assíncrona.
    private suspend fun carregarDetalhes(raca: Raca) {
        _estado.value = EstadoRaca.Sucesso(raca, EstadoDetalhes.Carregando)
        val detalhes = try {
            val resposta = RetrofitHelper.wikipediaApi.consultarArtigo()
            val texto = resposta.query.pages.firstOrNull()?.extract.orEmpty()
            EstadoDetalhes.Sucesso(selecionarSecoes(texto))
        } catch (erro: CancellationException) {
            // Cancelamento é parte do ciclo de vida, não uma falha de conexão.
            throw erro
        } catch (_: Exception) {
            EstadoDetalhes.Erro
        }
        _estado.value = EstadoRaca.Sucesso(raca, detalhes)
    }
}
