package com.rodrigojscript.agendawow.model.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface AgendaDAO {
    @Query("SELECT * FROM contactos ORDER BY id DESC")
    fun getContactos(): LiveData<MutableList<AgendaEntity.Contacto>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(contactos: AgendaEntity.Contacto)

    @Delete
    suspend fun delete(contactos: AgendaEntity.Contacto)

    @Update
    suspend fun update(contactos: AgendaEntity.Contacto)

    @Query("DELETE FROM contactos")
    suspend fun deleteAllData()
}