package br.edu.ifal.cavalos.domain

import androidx.room.Entity
import androidx.room.PrimaryKey

// Cada objeto representa uma linha da tabela; id identifica o registro sem repetição.
@Entity(tableName = "treinos_equestres")
data class Treino(
    @PrimaryKey val id: Int,
    val nome: String,
    val duracaoMinutos: Int,
    val foco: String
)
