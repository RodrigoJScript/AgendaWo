package com.rodrigojscript.agendawow.model

import android.app.Application
import androidx.lifecycle.LiveData
import com.rodrigojscript.agendawow.model.database.AgendaDAO
import com.rodrigojscript.agendawow.model.database.AgendaDatabase
import com.rodrigojscript.agendawow.model.database.AgendaEntity

class AgendaRepository(application: Application) {
    private var agendaDAO: AgendaDAO

    init {
        val database = AgendaDatabase.getInstance(application)
        agendaDAO = database.agendaDAO()
    }

    val readAllData: LiveData<MutableList<AgendaEntity.Contacto>> = agendaDAO.getContactos()

    suspend fun insertData(datos: AgendaEntity.Contacto) {
        agendaDAO.insert(datos)
    }

    suspend fun deleteData(datos: AgendaEntity.Contacto) {
        agendaDAO.delete(datos)
    }

    suspend fun updateData(datos: AgendaEntity.Contacto) {
        agendaDAO.update(datos)
    }

    suspend fun deleteAllData() {
        agendaDAO.deleteAllData()
    }
}