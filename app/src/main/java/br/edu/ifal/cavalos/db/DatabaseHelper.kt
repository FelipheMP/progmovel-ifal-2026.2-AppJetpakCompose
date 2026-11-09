package br.edu.ifal.cavalos.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import br.edu.ifal.cavalos.domain.Treino

@Database(entities = [Treino::class], version = 1, exportSchema = false)
abstract class DatabaseHelper : RoomDatabase() {
    abstract fun treinoDao(): TreinoDao

    companion object {
        fun getInstance(context: Context): DatabaseHelper =
            Room.databaseBuilder(context.applicationContext, DatabaseHelper::class.java, "cavalos.db")
                .addCallback(object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        // Usa a conexão em criação para carregar os exemplos antes da primeira leitura.
                        treinosDeExemplo.forEach { treino ->
                            db.execSQL(
                                "INSERT INTO treinos_equestres (id, nome, duracaoMinutos, foco) VALUES (?, ?, ?, ?)",
                                arrayOf(treino.id, treino.nome, treino.duracaoMinutos, treino.foco)
                            )
                        }
                    }
                })
                .build()
    }
}
