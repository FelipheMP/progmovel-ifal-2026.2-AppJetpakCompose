package br.edu.ifal.cavalos.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import br.edu.ifal.cavalos.db.DatabaseHelper
import br.edu.ifal.cavalos.domain.Treino
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface EstadoTreinos {
    data object Carregando : EstadoTreinos
    data class Sucesso(val treinos: List<Treino>) : EstadoTreinos
    data object Erro : EstadoTreinos
}

class TreinosViewModel(application: Application) : AndroidViewModel(application) {
    // AndroidViewModel fornece Application, um contexto que não depende da Activity.
    private val banco = DatabaseHelper.getInstance(application)
    private val _estado = MutableStateFlow<EstadoTreinos>(EstadoTreinos.Carregando)
    val estado = _estado.asStateFlow()

    // A consulta começa na criação do ViewModel, e não dentro da função de interface.
    init {
        // Room executa o DAO suspend sem bloquear a thread principal.
        viewModelScope.launch {
            try {
                val treinos = banco.treinoDao().listarTreinos()
                _estado.value = EstadoTreinos.Sucesso(treinos)
            } catch (erro: CancellationException) {
                throw erro
            } catch (_: Exception) {
                _estado.value = EstadoTreinos.Erro
            }
        }
    }

    // Esta instância do banco pertence ao ViewModel; fechamos quando ele é descartado.
    override fun onCleared() {
        banco.close()
        super.onCleared()
    }
}
