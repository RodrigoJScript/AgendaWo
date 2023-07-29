package com.rodrigojscript.agendawow.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.rodrigojscript.agendawow.model.AgendaRepository
import com.rodrigojscript.agendawow.model.database.AgendaEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AgendaViewModel(application: Application) : AndroidViewModel(application) {
    private val agendaRepository: AgendaRepository = AgendaRepository(application)

    fun getAllData(): LiveData<MutableList<AgendaEntity.Contacto>> {
        return agendaRepository.readAllData
    }

    fun insertData(datos: AgendaEntity.Contacto) {
        viewModelScope.launch(Dispatchers.IO) {
            agendaRepository.insertData(datos)
        }
    }

    fun deleteData(datos: AgendaEntity.Contacto) {
        viewModelScope.launch(Dispatchers.IO) {
            agendaRepository.deleteData(datos)
        }
    }

    fun deleteAllData() {
        viewModelScope.launch(Dispatchers.IO) {
            agendaRepository.deleteAllData()
        }
    }

    fun updateData(datos: AgendaEntity.Contacto) {
        viewModelScope.launch(Dispatchers.IO) {
            agendaRepository.updateData(datos)
        }
    }
}