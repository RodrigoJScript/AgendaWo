package com.rodrigojscript.agendawow.model.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

class AgendaEntity {
    @Entity(tableName = "contactos")
    data class Contacto(
        @PrimaryKey(autoGenerate = true) var id: Int?,

        @ColumnInfo(name = "name") var name: String,

        @ColumnInfo(name = "lastName") var lastName: String,

        @ColumnInfo(name = "email") var email: String,

        @ColumnInfo(name = "phone") var phone: Int,

        )
}