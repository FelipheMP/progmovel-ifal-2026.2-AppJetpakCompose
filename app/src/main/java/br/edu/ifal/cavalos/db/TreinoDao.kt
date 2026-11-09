package br.edu.ifal.cavalos.db

import androidx.room.Dao
import androidx.room.Query
import br.edu.ifal.cavalos.domain.Treino

// DAO reúne as consultas SQL; Room gera a implementação desta interface.
@Dao
interface TreinoDao {
    @Query("SELECT * FROM treinos_equestres ORDER BY id")
    suspend fun listarTreinos(): List<Treino>
}
