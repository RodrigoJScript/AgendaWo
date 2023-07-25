package com.rodrigojscript.agendawow.model.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [AgendaEntity.Contacto::class], version = 1, exportSchema = false)
abstract class AgendaDatabase : RoomDatabase() {
    abstract fun agendaDAO(): AgendaDAO

    companion object {
        private var INSTANCE: AgendaDatabase? = null
        fun getInstance(context: Context): AgendaDatabase {
            if (INSTANCE == null) {
                INSTANCE =
                    Room.databaseBuilder(context, AgendaDatabase::class.java, "roombd").build()
            }
            return INSTANCE as AgendaDatabase
        }
    }
}