package br.com.fiap.jmail.database.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import br.com.fiap.jmail.model.Contato

@Database(
    entities = [Contato::class],
    version = 1
)
abstract class ContatoDb : RoomDatabase() {

    abstract fun contatoDao(): ContatoDao

    companion object {

        private lateinit var instance: ContatoDb

        fun getDatabase(context: Context): ContatoDb {
            if (!::instance.isInitialized) {
                instance =
                    Room.databaseBuilder(
                        context,
                        ContatoDb::class.java,
                        "contato_db"
                    )
                        .allowMainThreadQueries()
                        .fallbackToDestructiveMigration() //somente teste rs rs
                        .build()
            }
            return instance
        }
    }
}